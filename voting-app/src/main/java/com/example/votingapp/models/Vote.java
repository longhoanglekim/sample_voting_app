package com.example.votingapp.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Data
@Setter
@Getter
public class Vote {
   private String voterId;
   private String choice;
}
