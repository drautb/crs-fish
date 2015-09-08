package io.github.drautb.crs.fish;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker;

import io.github.drautb.crs.fish.impl.CRSFishImpl;

@WebListener
public class Launcher implements ServletContextListener {

  private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

  private static final String DOMAIN = "drautb-test";

  @Override
  public void contextInitialized(ServletContextEvent event) {
    LOG.info("Registering decider and activity workers.");

    AmazonSimpleWorkflow swfClient = new AmazonSimpleWorkflowClient();

    try {
      WorkflowWorker ww = new WorkflowWorker(swfClient, DOMAIN, "crs-fish-tasks");
      ww.addWorkflowImplementationType(CRSFishImpl.Decider.class);
      ww.start();

      ActivityWorker aw = new ActivityWorker(swfClient, DOMAIN, "crs-fish-activities");
      aw.addActivitiesImplementation(new CRSFishImpl.Activities());
      aw.start();
    }
    catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {

  }

}
