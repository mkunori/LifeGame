package com.mkunori.lifegame.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * APIで発生した例外を、HTTPレスポンスに変換するクラスです。
 *
 * 不正なリクエストによって発生した例外を、
 * 400 Bad Request としてクライアントへ返します。
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * 不正な引数による例外を400 Bad Requestとして返します。
     *
     * rows / cols の範囲外、cells の空配列、cells の件数超過など、
     * リクエスト内容が正しくない場合に使われます。
     *
     * @param exception 発生した例外
     * @return エラー内容を含むレスポンス
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException exception) {
        Map<String, String> body = Map.of(
                "error", "Bad Request",
                "message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}