package data_access;

import org.json.JSONArray;
import use_case.DataAccessException;

/** Anything that can answer an API-NBA request with its "response" array.
 */
public interface ApiGateway {

  JSONArray get(String path) throws DataAccessException;
}
