package org.example.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TestResultLogger implements TestWatcher {

    private static final Logger logger = LoggerFactory.getLogger(TestResultLogger.class);

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logger.info("Test Disabled: {} - Reason: {}", context.getDisplayName(), reason.orElse("No reason provided"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logger.info("Test Passed: {}", context.getDisplayName());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.warn("Test Aborted: {} - Cause: {}", context.getDisplayName(), cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.error("Test Failed: {} - Error: {}", context.getDisplayName(), cause.getMessage(), cause);
    }
}
