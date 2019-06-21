package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import static uk.gov.ons.census.fwmt.outcomeservice.config.GatewayEventsConfig.OUTCOME_SENT_RM;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import uk.gov.ons.census.fwmt.common.data.comet.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.data.rm.OutcomeEvent;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.factory.FulfilmentRequestFactory;
import uk.gov.ons.census.fwmt.outcomeservice.factory.OutcomeEventFactory;
import uk.gov.ons.census.fwmt.outcomeservice.message.GatewayOutcomeProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

@Service
public class OutcomeServiceImpl implements OutcomeService {

  @Autowired
  private GatewayOutcomeProducer gatewayOutcomeProducer;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private OutcomeEventFactory outcomeEventFactory;

  @Autowired
  private FulfilmentRequestFactory fulfilmentRequestFactory;

  public void createHouseHoldOutcomeEvent(HouseholdOutcome householdOutcome) throws GatewayException {
    String outcomeEventText = createOutcomeEventText(householdOutcome);
    
    if (householdOutcome.getFulfillmentRequests() == null) {
      OutcomeEvent outcomeEvent = outcomeEventFactory.createOutcomeEvent(householdOutcome);

      if (outcomeEvent.getEvent().getType().equals("ADDRESS_NOT_VALID") || outcomeEvent.getEvent().getType()
          .equals("ADDRESS_TYPE_CHANGED")) {
        gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent);

      } else if (outcomeEvent.getEvent().getType().equals("REFUSAL_RECEIVED")) {
        gatewayOutcomeProducer.sendRespondentRefusal(outcomeEvent);
      }

      gatewayEventManager
          .triggerEvent(String.valueOf(outcomeEvent.getPayload().getInvalidAddress().getCollectionCase().getId()),
              OUTCOME_SENT_RM, LocalTime.now());

    } else if (!householdOutcome.getFulfillmentRequests().isEmpty()) {

      OutcomeEvent[] processedFulfilmentRequests = fulfilmentRequestFactory.createFulfilmentEvents(householdOutcome);

      for (OutcomeEvent outcomeEvent : processedFulfilmentRequests) {
        if (outcomeEvent.getEvent().getType().equals("FULFILMENT_REQUESTED"))
          gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent);

        if (outcomeEvent.getEvent().getType().equals("QUESTIONNAIRE_LINKED"))
          gatewayOutcomeProducer.sendFulfilmentRequest(outcomeEvent);
      }
    }
  }

  public static String createOutcomeEventText(HouseholdOutcome householdOutcome) {
    String oe = "";
    if (householdOutcome.getFulfillmentRequests()==null) {
      switch (householdOutcome.getSecondaryOutcome()) {
      case "Hard Refusal":
      case "Extraordinary Refusal":
        try {
          Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
          cfg.setClassForTemplateLoading(OutcomeServiceImpl.class, "/templates/");
          cfg.setDefaultEncoding("UTF-8");
          cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
          cfg.setLogTemplateExceptions(false);
          cfg.setWrapUncheckedExceptions(true);
          Map<String, Object> root = new HashMap<>();
          root.put("householdOutcome", householdOutcome);
          
          Template temp = cfg.getTemplate("payload/REFUSAL_RECEIVED-event.ftl");
          try (StringWriter out = new StringWriter(); StringWriter eventout = new StringWriter()) {

            temp.process(root, out);

            out.flush();
            oe = out.toString();
    
            root.put("payload", oe);
            root.put("eventType", "REFUSALTHING");
            
            Template etemp = cfg.getTemplate("rm-event.ftl");
            etemp.process(root, eventout);

            eventout.flush();
            System.out.println(eventout.toString());

            
            
        } catch (TemplateException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      break;

      default:
        break;
      }
    }
//    System.out.println(oe);
    return oe;
  }
}
