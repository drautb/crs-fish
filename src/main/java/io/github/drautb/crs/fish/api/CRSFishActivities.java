package io.github.drautb.crs.fish.api;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.ExponentialRetry;

@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = 30,
                             defaultTaskStartToCloseTimeoutSeconds = 24 * 3600,
                             defaultTaskList = "crs-fish-activities")
@Activities(activityNamePrefix = "CRSFish.",
            version = "2.0")
public interface CRSFishActivities {

  /**
   * Obtains approval for a color change request.
   *
   * @param   newColor for the fish.
   * @return  Boolean - True for approved, false otherwise.
   */
  @ExponentialRetry(initialRetryIntervalSeconds = 5,
                    maximumAttempts = 3,
                    exceptionsToRetry = AmazonServiceException.class)
  Boolean obtainApproval(String newColor);

  /**
   * Change the color of the fish.
   *
   * @param   newColor for the fish.
   */
  @ExponentialRetry(initialRetryIntervalSeconds = 5,
                    maximumAttempts = 3,
                    exceptionsToRetry = AmazonServiceException.class)
  void applyChange(String newColor);

}
