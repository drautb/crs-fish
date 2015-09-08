package io.github.drautb.crs.fish.impl;

import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.annotations.ManualActivityCompletion;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

import io.github.drautb.crs.fish.FishStore;
import io.github.drautb.crs.fish.api.CRSFishActivities;
import io.github.drautb.crs.fish.api.CRSFishActivitiesClient;
import io.github.drautb.crs.fish.api.CRSFishActivitiesClientImpl;
import io.github.drautb.crs.fish.api.CRSFishDecider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CRSFishImpl {

  private static final Logger LOG = LoggerFactory.getLogger(CRSFishImpl.class);

  /**
   * Decider Implementation
   */
  public static class Decider implements CRSFishDecider {

    // Activities client
    private final CRSFishActivitiesClient activities = new CRSFishActivitiesClientImpl();

    @Override
    public Promise<Void> changeColor(String newColor) {
      Promise<Boolean> approved = activities.obtainApproval(newColor);

      return waitForApproval(approved, newColor);
    }

    /**
     * If you annotate a method in a decider with @Asynchronous, and that method takes
     * 1 or more Promise objects as parameters, then SWF will wait until the Promises
     * are fulfilled before invoking the method.
     */
    @Asynchronous
    private Promise<Void> waitForApproval(Promise<Boolean> approvedPromise, String newColor) {
      Boolean approved = approvedPromise.get();

      if (approved) {
        return activities.applyChange(newColor);
      }

      return Promise.Void();
    }
  }

  /**
   * Activities Implementation
   */
  public static class Activities implements CRSFishActivities {

    @ManualActivityCompletion
    @Override
    public Boolean obtainApproval(String newColor) {
      ActivityExecutionContextProvider contextProvider = new ActivityExecutionContextProviderImpl();
      ActivityExecutionContext executionContext = contextProvider.getActivityExecutionContext();

      String taskToken = executionContext.getTaskToken();

      LOG.info("\n*** NEED APPROVAL FOR COLOR CHANGE ***\nNew Color: " + newColor + "\nTask Token: " + taskToken + "\n");

      // Ignored because of @ManualActivityCompletion annotation.
      return Boolean.TRUE;
    }

    @Override
    public void applyChange(String newColor) {
      FishStore.changeColor(newColor);
    }

  }

}
