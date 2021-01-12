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

public enum CustomExceptionMessage {

    //400, "Bad Request"
    USERNAME_IS_EMPTY(40001, "请输入用户名"),
    PASSWORD_IS_EMPTY(40002, "请输入密码"),
    TOKEN_IS_ERROR(40003, "请输入正确TOKEN"),
    USER_EXISTS(40004, "用户名已存在"),
    HEADER_ERROR(40005, "表头不匹配"),
    EXCEEDING_MAX_VALUE(40006, "超过最大值"),
    NAME_EXISTS(40007, "名称已存在"),
    PARAMETER_NOT_CORRECT(40008, "参数不符合格式"),
    TABLE_ID_IS_EMPTY(40009, "请输入表ID"),
    FIELD_ID_IS_EMPTY(40010, "请输入字段ID"),
    OPERATOR_ID_IS_EMPTY(40011, "请输入算子ID"),
    TABLE_NAME_IS_EMPTY(40012, "请输入表名"),
    CATALOG_LEVEL_OVER_LIMIT(40013, "编目层级超过限制"),
    CATALOG_NAME_IS_EMPTY(40014, "请输入编目名"),
    FIELD_IS_EMPTY(40015, "请添加字段"),
    OPERATOR_NAME_IS_EMPTY(40016, "请输入算子名称"),
    SOURCE_NAME_IS_EMPTY(40017, "请输入来源名称"),

    FILE_IS_EMPTY(40018, "请选择文件上传"),
    CURRENT_PASSWORD_ERROR(40019, "当前密码错误"),
    USERNAME_OR_PASSWORD_ERROR_DOES_NOT_MATCH(40020, "用户名或密码错误"),

    ROLE_ID_IS_EMPTY(40021, "角色不允许为空！"),


    //401
    USER_DOES_NOT_MATCH(40101, "用户不存在"),
    PASSWORD_DOES_NOT_MATCH(40102, "密码错误"),
//    SESSION_TIMEOUT(40104, "登录过期，需要重新登录！"),
    USER_AUTHENTICATION(40104, "需要登录！"),
    TOKEN_ERROR(40105, "无效TOKEN！"),

    //403
    RESOURCE_UNAUTHORIZED(40301, "资源未被授权"),

    //404, "Not Found"
    TABLE_ID_DOES_NOT_EXIST(40401, "表  不存在"),
    PROJECT_DOES_NOT_EXIST(40402, "项目 不存在"),

    //
    //409, "Conflict"

    USERNAME_ALREADY_EXIST(40901, "管理员 已经存在"),


    //500, server error
    
//    PROFILE_RESULT_ERROR(500001, "分析结果数据错误"),
    USERNAME_VIEWER_RECORD_CURRENT_ADMIN_NOT_RIGHT(500001, "审核人不是本人"),
    HAS_CHILD_DATA(500002, "还存在数据子集，请清空后再删"),
    REVIEWER_IS_NOT_EXIST(500003,"审核人未配置"),
    ;
	

    private final int code;
    private final String message;

    CustomExceptionMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CustomExceptionMessage valueOf(int code) {
        CustomExceptionMessage[] messages = values();
        int len = values().length;
        for (int i = 0; i < len; i++) {
            CustomExceptionMessage message = messages[i];
            if (message.code == code) {
                return message;
            }
        }
        throw new IllegalArgumentException("No matching constant for ["
                + code + "]");
    }


    @Override
    public String toString() {
        return Integer.toString(code);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
