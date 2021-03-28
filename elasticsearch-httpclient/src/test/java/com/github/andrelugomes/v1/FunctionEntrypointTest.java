package com.github.andrelugomes.v1;

import com.github.andrelugomes.v1.model.Event;
import com.google.common.testing.TestLogHandler;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class FunctionEntrypointTest {

  private static final Logger logger = Logger.getLogger(FunctionEntrypoint.class.getName());

  private static final TestLogHandler logHandler = new TestLogHandler();

  @BeforeClass
  public static void beforeClass() {
    logger.addHandler(logHandler);
  }

  @Test
  public void functionsPubsubSubscribe_shouldPrintPubsubMessage() throws Exception {
    String encodedData = Base64.getEncoder().encodeToString(
        "hello".getBytes(StandardCharsets.UTF_8));

    Event message = new Event();
    message.setData(encodedData);

    new FunctionEntrypoint().accept(message, null);

    assertThat("hello").isEqualTo(logHandler.getStoredLogRecords().get(0).getMessage());
  }
}
