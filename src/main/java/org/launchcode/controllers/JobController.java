package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view

        // find any job with the given id and add it to the job detail view
        Job someJob = jobData.findById(id);
            model.addAttribute("job", someJob);
            return "job-detail";

    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        // if there are errors, pass them to the view
        if (errors.hasErrors()){
            model.addAttribute("errors", errors);
            return "new-job";
        }

        else {

            // get the ID values from the select lists in the job form
            int jobEmployer = jobForm.getEmployerId();
            int jobLocation = jobForm.getLocationId();
            int jobPositionType = jobForm.getPositionTypeId();
            int jobCoreCompetency = jobForm.getCoreCompetencyId();

            // get the name from the job form
            String name = jobForm.getName();
            // get the employer, location, position type and core competencies from the job form by ID to ensure valid data
            Employer employer = jobData.getEmployers().findById(jobEmployer);
            Location location = jobData.getLocations().findById(jobLocation);
            PositionType positionType = jobData.getPositionTypes().findById(jobPositionType);
            CoreCompetency coreCompetency = jobData.getCoreCompetencies().findById(jobCoreCompetency);

            // create new job with the collected informatoin
            Job newJob = new Job(name, employer, location, positionType, coreCompetency);

            // add new job to jobData
            jobData.add(newJob);

            // pass new job into job detail view
            model.addAttribute(newJob);

            // redirect to new job by ID
            return "redirect:/job?id=" + newJob.getId();

        }

    }
}
