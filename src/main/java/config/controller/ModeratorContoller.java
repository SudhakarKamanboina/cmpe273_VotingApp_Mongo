package config.controller;

/**
 * Created by sudh on 2/21/2015.
 */

import config.beans.Moderator;
import config.beans.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import config.services.IModeratorService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.util.*;


@RestController
//@ComponentScan("services")
public class ModeratorContoller {

    @Autowired
    IModeratorService serviceObj;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public class BadRequestException extends IllegalArgumentException
    {
        public BadRequestException(String message)
        {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class CustomBadRequestException extends IllegalArgumentException
    {
        public CustomBadRequestException(String message)
        {
            super(message);
        }
    }

    @RequestMapping(value="/api/v1", method = RequestMethod.GET)
    public String showHomePage()
    {
        return "Voting App... Home Page";
    }

    @RequestMapping(value="/api/v1/moderators", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Moderator createModerator(@Valid @RequestBody Moderator newMod, HttpServletRequest request, BindingResult results) {
        System.out.println("inside");
        if(null == newMod.getName() || "".equals(newMod.getName()))
            throw new CustomBadRequestException("Name cannot be null");
        if(results.hasErrors())
            throw new CustomBadRequestException("Request Parameters Cannot be Empty");
        return serviceObj.createModerator(newMod);
    }

    @RequestMapping(value="/api/v1/moderators/{id}", method = RequestMethod.GET)
    public @ResponseBody Moderator viewModerator(@PathVariable int id, HttpServletRequest request)
    {
        if(!authenticate(request))
            throw new BadRequestException("Authentication Failed");
        return serviceObj.viewModerator(id);
    }

    @RequestMapping(value="/api/v1/moderators/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Moderator updateModerator(@PathVariable int id, @RequestBody Moderator newMod, HttpServletRequest request)
    {
        if(!authenticate(request))
            throw new BadRequestException("Authentication Failed");
        Moderator mod = null;
        try{
            mod = serviceObj.updateModerator(id, newMod);
        }catch (NullPointerException e)
        {
            throw new CustomBadRequestException("Id does not exist");
        }
        return mod;
    }

    @RequestMapping(value="/api/v1/moderators/{id}/polls", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Map<String, Object> createPoll(@PathVariable int id, @RequestBody Poll newPoll, HttpServletRequest request)
    {
        if(!authenticate(request))
            throw new BadRequestException("Authentication Failed");

        Map<String, Object> pollMap = new LinkedHashMap<String, Object>();
        try {
            Poll poll = serviceObj.createPoll(id, newPoll);
            pollMap.put("id", poll.getId());
            pollMap.put("question", poll.getQuestion());
            pollMap.put("started_at", poll.getStarted_at());
            pollMap.put("expired_at", poll.getExpired_at());
            pollMap.put("choice", poll.getChoice());
        }
        catch (NullPointerException e)
        {
            throw new CustomBadRequestException("Id does not exist");
        }
        return pollMap;
    }

    @RequestMapping(value="/api/v1/moderators/{modId}/polls/{pollId}", method = RequestMethod.GET)
    public @ResponseBody Poll viewPoll(@PathVariable int modId, @PathVariable String pollId, HttpServletRequest request)
    {
        if(!authenticate(request))
            throw new BadRequestException("Authentication Failed");
        return serviceObj.viewPoll(modId, pollId);
    }

    //view Poll without result
    @RequestMapping(value="/api/v1/polls/{pollId}", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> viewPollWithoutResult(@PathVariable String pollId)
    {
        Map<String, Object> pollMap = new LinkedHashMap<String, Object>();
        try {
            Poll poll = serviceObj.viewPollWithoutResult(pollId);
            pollMap.put("id", poll.getId());
            pollMap.put("question", poll.getQuestion());
            pollMap.put("started_at", poll.getStarted_at());
            pollMap.put("expired_at", poll.getExpired_at());
            pollMap.put("choice", poll.getChoice());
        }
        catch (NullPointerException e)
        {
            throw new CustomBadRequestException("Id does not exist");
        }
        return pollMap;
    }

    @RequestMapping(value="/api/v1/moderators/{modId}/polls", method = RequestMethod.GET)
    public @ResponseBody List<Poll> listPolls(@PathVariable int modId, HttpServletRequest request)
    {
        if(!authenticate(request))
            throw new BadRequestException("Authentication Failed");
        return serviceObj.listPolls(modId);
    }

    @RequestMapping(value="/api/v1/moderators/{modId}/polls/{pollId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoll(@PathVariable int modId, @PathVariable String pollId, HttpServletRequest request)
    {
        if(!authenticate(request))
            throw new BadRequestException("Authentication Failed");

        try {
            serviceObj.deletePoll(modId, pollId);
        }
        catch (NullPointerException e)
        {
            throw new CustomBadRequestException("Id does not exist");
        }
    }

    @RequestMapping(value="/api/v1/polls/{pollId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void voteAPoll(@PathVariable String pollId, @RequestParam(value="choice") int choice)
    {
        try {
            serviceObj.voteAPoll(pollId, choice);
        }
        catch (NullPointerException e)
        {
            throw new CustomBadRequestException("Id does not exist");
        }
    }

    public boolean authenticate(HttpServletRequest req)
    {
        String authorization = req.getHeader("Authorization");
        if(null == authorization || "".equals(authorization))
            return false;
        String credentials = authorization.substring("Basic".length()).trim();
        byte[] decoded = DatatypeConverter.parseBase64Binary(credentials);
        String decodedString = new String(decoded);
        String[] actualCredentials = decodedString.split(":");
        String ID = actualCredentials[0];
        String Password = actualCredentials[1];
        return (ID.equals("foo") && Password.equals("bar"));
    }
}
