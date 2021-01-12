/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package seassoon.rule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

    CustomException(String message) {
        super(message);
    }

    CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class NotFoundException extends CustomException {
        public NotFoundException(CustomExceptionMessage message) {
            super(message.toString());
        }
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    public static class ConflictException extends CustomException {
        public ConflictException(CustomExceptionMessage message) {
            super(message.toString());
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class BadRequestException extends CustomException {
        public BadRequestException(CustomExceptionMessage message) {
            super(message.toString());
        }
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public static class AuthorizationException extends CustomException {
        public AuthorizationException(CustomExceptionMessage message) {
            super(message.toString());
        }
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public static class AuthenticationException extends CustomException {
        public AuthenticationException(CustomExceptionMessage message) {
            super(message.toString());
        }
    }
    public static class ServiceException extends CustomException {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
        public ServiceException(String message) {
            super(message);
        }
    }

}
