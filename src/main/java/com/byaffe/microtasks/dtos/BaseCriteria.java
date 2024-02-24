package com.byaffe.microtasks.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseCriteria {
 
   private String searchTerm;
   private int offset=0;
   private int limit=0;

   public BaseCriteria(String searchTerm, int offset, int limit) {
      this.searchTerm = searchTerm;
      this.offset = offset;
      this.limit = limit;
   }
}