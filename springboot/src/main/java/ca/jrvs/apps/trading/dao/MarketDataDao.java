package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import javax.swing.text.html.parser.DTD;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String>{

  private static final String IEX_BATCH_PATH = "stock/market/batch?symbols=%s&types=quote&token=";
  private static final int HTTP_OK = 200;
  private final String IEX_BATCH_URL;

  private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private HttpClientConnectionManager httpClientConnectionManager;

  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig){
    this.httpClientConnectionManager = httpClientConnectionManager;
    IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  @Override
  public <S extends IexQuote> S save(S s) {
    return null;
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
    return null;
  }

  /**
   * Get a single IexQuote
   * @param ticker
   * @return IexQuote
   * @throws IllegalArgumentException if ticker is invalid
   * @throws DataRetrievalFailureException if Http reuquest fails
   */
  @Override
  public Optional<IexQuote> findById(String ticker) {
    Optional<IexQuote> iexQuote;
    List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

    if (quotes.size() == 0){
      return Optional.empty();
    } else if (quotes.size() ==1) {
      iexQuote = Optional.of(quotes.get(0));
    } else {
      throw new DataRetrievalFailureException("Unexpected number of quotes.");
    }
    return iexQuote;
  }

  @Override
  public boolean existsById(String s) {
    return false;
  }

  @Override
  public Iterable<IexQuote> findAll() {
    return null;
  }

  @Override
  public List<IexQuote> findAllById(Iterable<String> tickerList)
      throws IllegalArgumentException, DataRetrievalFailureException{
    int tickerNumber = 0;
    for (String ticker : tickerList){
      if (!ticker.matches("[a-zA-Z]{2,4}")){
        throw new IllegalArgumentException("Illegal ticker format.");
      }
      tickerNumber++;
    }
    if (tickerNumber==0){
      throw new IllegalArgumentException("No ticker found.");
    }
    List<IexQuote> quotes = new ArrayList<>();
    String tickerListString = String.join(",", tickerList);
    String url = String.format(IEX_BATCH_URL, tickerListString);
    Optional<String> quotesString = executeHttpGet(url);
    JSONObject jsonObject = new JSONObject(quotesString.get());
    ObjectMapper mapper = new ObjectMapper();
    IexQuote quote;
    for (String ticker : tickerList){
      String quoteString = jsonObject.getJSONObject(ticker).getJSONObject("quote").toString();
      try {
        quote = mapper.readValue(quoteString, IexQuote.class);
      }catch (IOException e){
        throw new RuntimeException("Connot convert JSON to quote object.");
      }
      quotes.add(quote);
    }

    return quotes;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(String s) {

  }

  @Override
  public void delete(IexQuote iexQuote) {

  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> iterable) {

  }

  @Override
  public void deleteAll() {

  }

  /**
   * Execute a get and return http entity/body as a string
   * @param url of resource
   * @return http response body
   * @throws DataRetrievalFailureException if fail
   */
  private Optional<String> executeHttpGet(String url)
      throws DataRetrievalFailureException {
    HttpClient httpClient = getHttpClient();
    HttpGet httpRequest = new HttpGet(url);
    HttpResponse httpResponse;
    try {
      httpResponse = httpClient.execute(httpRequest);
    }catch (IOException e){
      throw new RuntimeException("The url is not valid.");
    }
    int status = httpResponse.getStatusLine().getStatusCode();
    if(status != HTTP_OK){
      throw new DataRetrievalFailureException("Unexpected HTTP status: " + status);
    }
    if (httpResponse.getEntity() == null){
      throw new RuntimeException("Empty response body.");
    }
    String jsonString;
    try{
      HttpEntity httpEntity = httpResponse.getEntity();
      jsonString = EntityUtils.toString(httpEntity);
    }catch (IOException e){
      throw new RuntimeException("Failed to convert from entity to String", e);
    }
    Optional<String> response = Optional.of(jsonString);
    return response;
  }

  /**
   * Borrow a Http client form httpClientConnectionManager
   * @return httpClient
   */
  private CloseableHttpClient getHttpClient(){
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }
}
