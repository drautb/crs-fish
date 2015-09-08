package io.github.drautb.crs.fish;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.drautb.crs.fish.api.CRSFishDeciderClientExternal;
import io.github.drautb.crs.fish.api.CRSFishDeciderClientExternalFactory;
import io.github.drautb.crs.fish.api.CRSFishDeciderClientExternalFactoryImpl;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.JsonDataConverter;
import com.amazonaws.services.simpleworkflow.flow.ManualActivityCompletionClient;
import com.amazonaws.services.simpleworkflow.flow.ManualActivityCompletionClientFactory;
import com.amazonaws.services.simpleworkflow.flow.ManualActivityCompletionClientFactoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CRSServlet extends HttpServlet {

  private static final Logger LOG = LoggerFactory.getLogger(CRSServlet.class);

  private AmazonSimpleWorkflow swfClient = new AmazonSimpleWorkflowClient();
  private JsonDataConverter dataConverter = new JsonDataConverter();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setStatus(HttpServletResponse.SC_OK);
    try (PrintWriter writer = resp.getWriter()) {
      writer.write(FishStore.getColor());
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setStatus(HttpServletResponse.SC_OK);
    String path = req.getServletPath();

    if (path.equals("/crs/submit")) {
      String newColor = req.getParameter("color");

      CRSFishDeciderClientExternalFactory clientFactory = new CRSFishDeciderClientExternalFactoryImpl(swfClient, "drautb-test");
      CRSFishDeciderClientExternal workflowClient = clientFactory.getClient();
      workflowClient.changeColor(newColor);

      return;
    }

    String taskToken = req.getParameter("id");
    Boolean approved = Boolean.FALSE;
    if (path.equals("/crs/approve")) {
      approved = Boolean.TRUE;
    }

    LOG.info("Completing activity with status approved=" + approved + " and task token '" + taskToken + "'");

    ManualActivityCompletionClientFactory manualCompletionClientFactory = new ManualActivityCompletionClientFactoryImpl(swfClient);
    ManualActivityCompletionClient manualCompletionClient = manualCompletionClientFactory.getClient(taskToken);
    manualCompletionClient.complete(dataConverter.toData(approved));
  }

}
