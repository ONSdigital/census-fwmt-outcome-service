package uk.gov.ons.census.fwmt.outcomeservice.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.outcomeservice.enums.EventType;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.OutcomeServiceImpl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Component
public class TemplateCreator {

  public static String createOutcomeMessage(EventType eventType, Map<String, Object> root) {
    String outcomeMessage = "";

    try {
      Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
      configuration.setClassForTemplateLoading(OutcomeServiceImpl.class, "/templates/");
      configuration.setDefaultEncoding("UTF-8");
      configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      configuration.setLogTemplateExceptions(false);
      configuration.setWrapUncheckedExceptions(true);

      Template temp = configuration.getTemplate("payload/" + eventType.name() + "-event.ftl");
      try (StringWriter out = new StringWriter(); StringWriter outcomeEventMessage = new StringWriter()) {

        temp.process(root, out);

        out.flush();
        String outcomePayload = out.toString();

        root.put("payload", outcomePayload);
        root.put("eventType", eventType);
        Template outcomeMessageTemplate = configuration.getTemplate("rm-outcome-event.ftl");
        outcomeMessageTemplate.process(root, outcomeEventMessage);

        outcomeEventMessage.flush();
        outcomeMessage = outcomeEventMessage.toString();

      } catch (TemplateException e) {
        log.error("Error: ", e);
      }
    } catch (IOException e) {
      log.error("Error: ", e);
    }

    return outcomeMessage;
  }
}
