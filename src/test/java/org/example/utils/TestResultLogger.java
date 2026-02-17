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
        String subject = "Test Passed: " + context.getDisplayName();
        String body = "The test '" + context.getDisplayName() + "' passed successfully.";

        NotificationManager.sendEmail(subject, body);
        NotificationManager.sendSlackMessage(subject + "\n" + body);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.warn("Test Aborted: {} - Cause: {}", context.getDisplayName(), cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.error("Test Failed: {} - Error: {}", context.getDisplayName(), cause.getMessage(), cause);
        String subject = "Test Failed: " + context.getDisplayName();
        String body = "The test '" + context.getDisplayName() + "' failed.\n\nError: " + cause.getMessage()
                + "\n\nStack Trace:\n" + getStackTrace(cause);

        NotificationManager.sendEmail(subject, body);
        NotificationManager.sendSlackMessage(subject + "\n" + body);
    }

    private String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
