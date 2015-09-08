package io.github.drautb.crs.fish.api;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

@Workflow()
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = 24 * 3600,
                             defaultTaskList = "crs-fish-tasks")
public interface CRSFishDecider {

  /**
   * Executes the process for approving and carrying out a fish color change request.
   *
   * @param  newColor for the fish.
   */
  @Execute(version = "1.0")
  Promise<Void> changeColor(String newColor);

}
