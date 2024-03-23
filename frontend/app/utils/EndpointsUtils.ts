let resolveBackendEndpoint = 'http://localhost:8080/api';

if (typeof window !== 'undefined' && window.location.hostname === 'localhost') {
    resolveBackendEndpoint = 'http://localhost:8080/api';
} else if (typeof window !== 'undefined' && window.location.hostname.includes('learningkingv2.us-east-1.elasticbeanstalk.com')) {
    resolveBackendEndpoint = 'http://learningkingv2.us-east-1.elasticbeanstalk.com/api';
}

export const BASE_URL_ENDPOINT_PATH = resolveBackendEndpoint;
