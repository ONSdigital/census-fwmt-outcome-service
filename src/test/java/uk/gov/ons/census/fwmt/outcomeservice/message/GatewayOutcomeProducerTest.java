package uk.gov.ons.census.fwmt.outcomeservice.message;

//@RunWith(MockitoJUnitRunner.class)
public class GatewayOutcomeProducerTest {

  //  @InjectMocks
  //  GatewayOutcomeProducer gatewayOutcomeProducer;
  //
  //  @Mock
  //  RabbitTemplate rabbitTemplate;
  //
  //  @Mock
  //  ObjectMapper objectMapper;

  //  @Test
  //  public void sendAddressUpdateTest() throws JsonProcessingException, GatewayException {
  //    //Given
  //    OutcomeEvent outcomeEvent = new OutcomeEvent();
  //    Event event = new Event();
  //    event.setTransactionId(UUID.fromString("c45de4dc-3c3b-11e9-b210-d663bd873d93"));
  //    outcomeEvent.setEvent(event);
  //
  //    final String responseJson = "{  \n"
  //        + "   \"event\":{  \n"
  //        + "      \"type\":\"ADDRESS_NOT_VALID\",\n"
  //        + "      \"transactionId\":\"c45de4dc-3c3b-11e9-b210-d663bd873d93\"\n"
  //        + "   },\n"
  //        + "   \"household\":{  \n"
  //        + "      \"invalidAddress\":{  \n"
  //        + "         \"reason\":\"Derelict\",\n"
  //        + "         \"collectionCase\":{  \n"
  //        + "            \"id\":\"6c9b1177-3e03-4060-b6db-f6a8456292ef\"\n"
  //        + "         }\n"
  //        + "      },\n"
  //        + "      \"refusal\":{  \n"
  //        + "         \"collectionCase\":{  \n"
  //        + "            \"id\":\"6c9b1177-3e03-4060-b6db-f6a8456292ef\"\n"
  //        + "         }\n"
  //        + "      },\n"
  //        + "      \"contact\":{  \n"
  //        + "\n"
  //        + "      }\n"
  //        + "   }\n"
  //        + "}";
  //
  //    when(objectMapper.writeValueAsString(eq(outcomeEvent))).thenReturn(responseJson);
  //
  //    //When
  //    gatewayOutcomeProducer.sendAddressUpdate(outcomeEvent);
  //
  //    //Then
  //    verify(objectMapper).writeValueAsString(eq(outcomeEvent));
  //    verify(rabbitTemplate).convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
  //        GatewayOutcomeQueueConfig.GATEWAY_ADDRESS_UPDATE_ROUTING_KEY, responseJson);
  //
  //  }
  //
  //  @Test
  //  public void sendRespondentRefusalTest() throws JsonProcessingException, GatewayException {
  //    //Given
  //    OutcomeEvent outcomeEvent = new OutcomeEvent();
  //    Event event = new Event();
  //    event.setTransactionId(UUID.fromString("c45de4dc-3c3b-11e9-b210-d663bd873d93"));
  //    outcomeEvent.setEvent(event);
  //
  //    final String responseJson = "{  \n"
  //        + "   \"event\":{  \n"
  //        + "      \"type\":\"REFUSAL_RECEIVED\",\n"
  //        + "      \"transactionId\":\"c45de4dc-3c3b-11e9-b210-d663bd873d93\"\n"
  //        + "   },\n"
  //        + "   \"household\":{  \n"
  //        + "      \"invalidAddress\":{  \n"
  //        + "         \"collectionCase\":{  \n"
  //        + "            \"id\":\"6c9b1177-3e03-4060-b6db-f6a8456292ef\"\n"
  //        + "         }\n"
  //        + "      },\n"
  //        + "      \"refusal\":{  \n"
  //        + "         \"type\":\"Hard Refusal\",\n"
  //        + "         \"collectionCase\":{  \n"
  //        + "            \"id\":\"6c9b1177-3e03-4060-b6db-f6a8456292ef\"\n"
  //        + "         }\n"
  //        + "      },\n"
  //        + "      \"contact\":{  \n"
  //        + "\n"
  //        + "      }\n"
  //        + "   }\n"
  //        + "}";
  //
  //    when(objectMapper.writeValueAsString(eq(outcomeEvent))).thenReturn(responseJson);
  //
  //    //When
  //    gatewayOutcomeProducer.sendRespondentRefusal(outcomeEvent);
  //
  //    //Then
  //    verify(objectMapper).writeValueAsString(eq(outcomeEvent));
  //    verify(rabbitTemplate).convertAndSend(GatewayOutcomeQueueConfig.GATEWAY_OUTCOME_EXCHANGE,
  //        GatewayOutcomeQueueConfig.GATEWAY_RESPONDENT_REFUSAL_ROUTING_KEY, responseJson);
  //
  //  }
}