package com.misha.payload.response;

import com.misha.model.Registration;

import java.time.LocalDateTime;

public class ResponseRegistration {
        private boolean success;
        private String message;
        private LocalDateTime timestamp;
        private RegistrationDTO data;
        public ResponseRegistration(boolean success, String message, RegistrationDTO data) {
            this.timestamp = LocalDateTime.now();
            this.success = success;
            this.message = message;
            this.data = data;
        }
        public ResponseRegistration(boolean success, String message)
        {
            this.timestamp = LocalDateTime.now();
            this.success = success;
            this.message = message;
        }
        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public RegistrationDTO getData() {
            return data;
        }

}
