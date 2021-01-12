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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(CustomException.ServiceException.class)
    public ResponseEntity handleGriffinExceptionOfServer(
            HttpServletRequest request,
            CustomException.ServiceException e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message, request.getRequestURI(), cause.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handleGriffinExceptionOfAuth(
            HttpServletRequest request,
            AuthorizationException e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.FORBIDDEN,
                "资源未授权", request.getRequestURI(), e.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleGriffinExceptionOfAuth(
            HttpServletRequest request,
            AuthenticationException e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                message, request.getRequestURI(), e.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(CustomException.class)
    public ResponseEntity handleGriffinExceptionOfClient(
            HttpServletRequest request, CustomException e) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(
                e.getClass(), ResponseStatus.class);
        HttpStatus status = responseStatus.code();
        String code = e.getMessage();
        CustomExceptionMessage message = CustomExceptionMessage
                .valueOf(Integer.valueOf(code));
        CustomExceptionResponse body = new CustomExceptionResponse(
                status, message, request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }


    @SuppressWarnings("rawtypes")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity runtimeException(
            HttpServletRequest request, RuntimeException e) {


        String message = e.getMessage();
        if(StringUtils.isBlank(message)){
            message="服务器错误！";
        }
        Throwable cause = e.getCause();
        e.printStackTrace();
        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message, request.getRequestURI(), e.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandle(
            HttpServletRequest request, Exception e) {
        log.error("系统错误", e);
        String message = e.getMessage();
        Throwable cause = e.getCause();

        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message, request.getRequestURI(), e.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity paramInvalid(MethodArgumentNotValidException e,   HttpServletRequest request) {

        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), request.getRequestURI(), e.getClass().getName());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity requestInvalid(HttpMessageNotReadableException e,    HttpServletRequest request) {
        CustomExceptionResponse body = new CustomExceptionResponse(
                HttpStatus.BAD_REQUEST,
                "请求输入参数格式不正确", request.getRequestURI(), e.getClass().getName());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);


    }
}
