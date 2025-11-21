package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.AnswerDao;
import com.byaffe.learningking.daos.QuestionDao;
import com.byaffe.learningking.daos.QuizDao;
import com.byaffe.learningking.dtos.quiz.BulkQuizQuestionUploadResponse;
import com.byaffe.learningking.dtos.quiz.QuizQuestionRequestDTO;
import com.byaffe.learningking.dtos.quiz.QuizRequestDTO;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.quizes.AnswerOption;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.models.quizes.QuestionResponseType;
import com.byaffe.learningking.models.quizes.Quiz;
import com.byaffe.learningking.services.CourseLectureService;
import com.byaffe.learningking.services.QuizService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    AnswerDao answerDao;

    @Autowired
    CourseLectureService lectureService;

    @Autowired
    ModelMapper modelMapper;

    public static Search generateSearchTermsForQuizes(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("title", "description"));
    }

    @Override
    public Quiz saveQuiz(QuizRequestDTO dto) throws ValidationFailedException {
        if (StringUtils.isEmpty(dto.getTitle())) {
            throw new ValidationFailedException("Missing title");
        }
        if (StringUtils.isEmpty(dto.getDescription())) {
            throw new ValidationFailedException("Missing description");
        }
        Quiz quiz = modelMapper.map(dto, Quiz.class);

        return quizDao.save(quiz);
    }

    @Override
    public List<Quiz> getQuizes(Search search, int offset, int limit) {
        search.setMaxResults(limit).setFirstResult(offset);
        return quizDao.search(search);
    }

    @Override
    public long countQuizes(Search search) {
        return quizDao.count(search);
    }

    @Override
    public Quiz getById(Long id) throws ValidationFailedException {
        return quizDao.findById(id).orElseThrow(() -> new ValidationFailedException("Record Not Found"));
    }

    @Override
    public Question saveQuizQuestion(QuizQuestionRequestDTO dto) throws ValidationFailedException {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ValidationFailedException("Missing title");
        }
        if (dto.getQuizId() == null) {
            throw new ValidationFailedException("Missing quiz id");
        }
        Question question = new Question();
        if (dto.getId() != null) {
            question = getQuestionById(dto.getId());
        }
        modelMapper.map(dto, question);
        question.setQuiz(getById(dto.getQuizId()));
        //todo some position reorganisation
        question = questionDao.save(question);
        for (QuizQuestionRequestDTO.AnswerRequestDTO answerDTO : dto.getAnswerOptions()) {
            AnswerOption answerOption  = new AnswerOption();
            if (answerDTO.getId() != null) {
                answerOption = getAnswerOptionById(dto.getId());
            }
             modelMapper.map(answerDTO, answerOption);
            answerOption.setQuestion(question);
            answerDao.save(answerOption);
        }

        return question;
    }


    @Override
    public List<Question> getQuizQuestions(Search search, int offset, int limit) {
        search.setMaxResults(limit).setFirstResult(offset);
        return questionDao.search(search);
    }

    @Override
    public long countQuizQuestions(Search search) {
        return questionDao.count(search);
    }

    public Question getQuestionById(Long id) throws ValidationFailedException {
        return questionDao.findById(id).orElseThrow(() -> new ValidationFailedException("Record Not Found"));
    }
    public AnswerOption getAnswerOptionById(Long id) throws ValidationFailedException {
        return answerDao.findById(id).orElseThrow(() -> new ValidationFailedException("Record Not Found"));
    }

    /**
     * Get all quizzes that are associated with lectures in a specific course
     * Demonstrates the use of bidirectional mapping for filtering
     */
    public List<Quiz> getQuizzesForCourse(Long courseId) {
        Search search = new Search();
        search.addFilterEqual("lectureQuizzes.lecture.courseTopic.courseLesson.course.id", courseId);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);
        
        // Add fetch joins for better performance
        search.addFetch("lectureQuizzes");
        search.addFetch("lectureQuizzes.lecture");
        search.addFetch("lectureQuizzes.lecture.courseTopic");
        search.addFetch("lectureQuizzes.lecture.courseTopic.courseLesson");
        search.addFetch("lectureQuizzes.lecture.courseTopic.courseLesson.course");
        
        return quizDao.search(search);
    }

    /**
     * Get all quizzes that are not associated with any lecture
     */
    public List<Quiz> getUnassignedQuizzes() {
        Search search = new Search();
        search.addFilterEmpty("lectureQuizzes");
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);
        
        return quizDao.search(search);
    }

    /**
     * Get all quizzes with their associated lectures
     */
    public List<Quiz> getQuizzesWithLectures() {
        Search search = new Search();
        search.addFilterNotEmpty("lectureQuizzes");
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("publicationStatus", PublicationStatus.ACTIVE);
        
        // Add fetch joins for better performance
        search.addFetch("lectureQuizzes");
        search.addFetch("lectureQuizzes.lecture");
        
        return quizDao.search(search);
    }

    @Override
    public BulkQuizQuestionUploadResponse uploadQuizQuestionsFromCsv(Long quizId, MultipartFile file) {
        if (quizId == null) {
            throw new ValidationFailedException("Missing quiz id");
        }
        if (file == null || file.isEmpty()) {
            throw new ValidationFailedException("Missing CSV file");
        }
        getById(quizId);

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreEmptyLines()
                    .withTrim()
                    .parse(reader);
            if (parser.getHeaderMap().isEmpty()) {
                throw new ValidationFailedException("CSV file must contain headers");
            }

            Map<String, String> headerLookup = buildHeaderLookup(parser.getHeaderMap().keySet());
            String questionHeader = headerLookup.get("question");
            if (questionHeader == null) {
                throw new ValidationFailedException("Missing 'question' column in CSV");
            }

            List<String> optionHeaders = headerLookup.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("option"))
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            if (optionHeaders.size() < 2) {
                throw new ValidationFailedException("CSV must contain at least two option columns (option1, option2, ...)");
            }

            String correctHeader = headerLookup.getOrDefault("correct", headerLookup.get("correct_option"));
            if (correctHeader == null) {
                throw new ValidationFailedException("Missing 'correct' column in CSV");
            }

            String marksHeader = headerLookup.get("marks");
            String responseTypeHeader = headerLookup.get("response_type");

            BulkQuizQuestionUploadResponse response = new BulkQuizQuestionUploadResponse();
            int processedRows = 0;
            for (CSVRecord record : parser) {
                processedRows++;
                try {
                    QuizQuestionRequestDTO questionDTO = buildQuestionDtoFromRecord(
                            record,
                            quizId,
                            questionHeader,
                            marksHeader,
                            responseTypeHeader,
                            optionHeaders,
                            correctHeader);
                    saveQuizQuestion(questionDTO);
                    response.setSuccessCount(response.getSuccessCount() + 1);
                } catch (Exception ex) {
                    response.getErrors().add(
                            String.format("Row %d: %s", record.getRecordNumber() + 1, ex.getMessage()));
                }
            }
            response.setTotalRows(processedRows);
            return response;
        } catch (IOException e) {
            throw new ValidationFailedException("Failed to read CSV file: " + e.getMessage());
        }
    }

    private QuizQuestionRequestDTO buildQuestionDtoFromRecord(CSVRecord record,
                                                              Long quizId,
                                                              String questionHeader,
                                                              String marksHeader,
                                                              String responseTypeHeader,
                                                              List<String> optionHeaders,
                                                              String correctHeader) {
        String questionText = record.get(questionHeader);
        if (StringUtils.isBlank(questionText)) {
            throw new ValidationFailedException("Question text is required");
        }

        QuizQuestionRequestDTO dto = new QuizQuestionRequestDTO();
        dto.setQuizId(quizId);
        dto.setName(questionText.trim());
        dto.setPosition((int) record.getRecordNumber());
        dto.setMarks(resolveMarks(record, marksHeader));

        List<QuizQuestionRequestDTO.AnswerRequestDTO> answers = buildAnswerOptions(record, optionHeaders);
        Set<Integer> correctIndexes = resolveCorrectIndexes(record.get(correctHeader), answers);
        if (correctIndexes.isEmpty()) {
            throw new ValidationFailedException("Unable to resolve any correct answer");
        }
        correctIndexes.forEach(index -> answers.get(index).setCorrect(true));
        dto.setAnswerOptions(answers);
        dto.setResponseType(resolveResponseType(record, responseTypeHeader, correctIndexes.size()));
        return dto;
    }

    private double resolveMarks(CSVRecord record, String marksHeader) {
        if (marksHeader == null) {
            return 1D;
        }
        String value = record.get(marksHeader);
        if (StringUtils.isBlank(value)) {
            return 1D;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            throw new ValidationFailedException("Invalid marks value '" + value + "'");
        }
    }

    private List<QuizQuestionRequestDTO.AnswerRequestDTO> buildAnswerOptions(CSVRecord record,
                                                                             List<String> optionHeaders) {
        List<QuizQuestionRequestDTO.AnswerRequestDTO> answers = new ArrayList<>();
        for (String header : optionHeaders) {
            String value = record.get(header);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            QuizQuestionRequestDTO.AnswerRequestDTO answerDTO = new QuizQuestionRequestDTO.AnswerRequestDTO();
            answerDTO.setName(value.trim());
            answers.add(answerDTO);
        }
        if (answers.size() < 2) {
            throw new ValidationFailedException("At least two answer options are required");
        }
        return answers;
    }

    private QuestionResponseType resolveResponseType(CSVRecord record, String responseTypeHeader, int correctCount) {
        if (responseTypeHeader == null) {
            return correctCount > 1 ? QuestionResponseType.MultipleChoice : QuestionResponseType.SingleChoice;
        }
        String value = record.get(responseTypeHeader);
        if (StringUtils.isBlank(value)) {
            return correctCount > 1 ? QuestionResponseType.MultipleChoice : QuestionResponseType.SingleChoice;
        }
        try {
            return QuestionResponseType.valueOf(value.trim());
        } catch (IllegalArgumentException ex) {
            throw new ValidationFailedException(String.format("Invalid response type '%s'", value));
        }
    }

    private Set<Integer> resolveCorrectIndexes(String rawValue, List<QuizQuestionRequestDTO.AnswerRequestDTO> answers) {
        Set<Integer> indexes = new LinkedHashSet<>();
        if (StringUtils.isBlank(rawValue)) {
            return indexes;
        }
        String[] tokens = rawValue.split("[,;|]");
        for (String token : tokens) {
            String normalized = token.trim();
            if (StringUtils.isBlank(normalized)) {
                continue;
            }
            Integer byNumber = parseIndexFromNumber(normalized, answers.size());
            if (byNumber != null) {
                indexes.add(byNumber);
                continue;
            }
            Integer byLetter = parseIndexFromLetter(normalized, answers.size());
            if (byLetter != null) {
                indexes.add(byLetter);
                continue;
            }
            int byText = findIndexByText(normalized, answers);
            if (byText >= 0) {
                indexes.add(byText);
            }
        }
        return indexes;
    }

    private Integer parseIndexFromNumber(String value, int totalOptions) {
        if (StringUtils.isNumeric(value)) {
            int idx = Integer.parseInt(value) - 1;
            return idx >= 0 && idx < totalOptions ? idx : null;
        }
        if (value.toLowerCase(Locale.ROOT).startsWith("option")) {
            String digits = value.replaceAll("\\D", "");
            if (StringUtils.isNumeric(digits)) {
                int idx = Integer.parseInt(digits) - 1;
                return idx >= 0 && idx < totalOptions ? idx : null;
            }
        }
        return null;
    }

    private Integer parseIndexFromLetter(String value, int totalOptions) {
        if (value.length() != 1) {
            return null;
        }
        char character = Character.toUpperCase(value.charAt(0));
        if (character < 'A' || character > 'Z') {
            return null;
        }
        int idx = character - 'A';
        return idx >= 0 && idx < totalOptions ? idx : null;
    }

    private int findIndexByText(String value, List<QuizQuestionRequestDTO.AnswerRequestDTO> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).getName().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return -1;
    }

    private Map<String, String> buildHeaderLookup(Set<String> headers) {
        Map<String, String> lookup = new LinkedHashMap<>();
        for (String header : headers) {
            if (header == null) {
                continue;
            }
            String normalized = normalizeHeader(header);
            lookup.putIfAbsent(normalized, header);
        }
        return lookup;
    }

    private String normalizeHeader(String header) {
        return header.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_");
    }
}
