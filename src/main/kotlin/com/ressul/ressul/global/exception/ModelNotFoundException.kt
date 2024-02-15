package com.ressul.ressul.global.exception

data class ModelNotFoundException(val modelName: String) :
    RuntimeException("Model $modelName not found")