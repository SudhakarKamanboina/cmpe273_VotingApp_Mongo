package config.services;

import config.beans.Moderator;
import config.beans.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sudh on 2/27/2015.
 */
@Service
public class ModeratorService implements IModeratorService
{
    @Autowired
    ModeratorRepository modRepository;

    private static Map<Integer, Moderator> dataStore = new HashMap<Integer, Moderator>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Random rd = new Random();

    @Override
    public Moderator createModerator(Moderator newMod) {
        int id = rd.nextInt(50000);
        newMod.setId(id);
        newMod.setCreated_at(dateFormat.format(new Date()));
        //dataStore.put(id, newMod);
        modRepository.save(newMod);
        return newMod;
    }

    @Override
    public Moderator viewModerator(int modId) {
        //return dataStore.get(modId) ;
        return modRepository.findOne(modId);
    }

    @Override
    public Moderator updateModerator(int id, Moderator newMod) {
        Moderator oldMod = modRepository.findOne(id); //dataStore.get(id);
        if(null!=newMod.getEmail() && !"".equals(newMod.getEmail()))
        {
            oldMod.setEmail(newMod.getEmail());
        }
        if(null!=newMod.getPassword() && !"".equals(newMod.getPassword()))
        {
            oldMod.setPassword(newMod.getPassword());
        }
        if(null!=newMod.getName() && !"".equals(newMod.getName()))
        {
            oldMod.setName(newMod.getName());
        }
        //dataStore.put(id, oldMod);
        modRepository.save(oldMod);
        return oldMod;
    }

    public Poll createPoll(int id, Poll newPoll) {

        Random rd = new Random();
        String newId = generateAlphaNumAuthCode();
        newPoll.setId(newId);

        Moderator original = modRepository.findOne(id);  //dataStore.get(id);
        original.setPoll(newId, newPoll);
        modRepository.save(original);
        return newPoll;
    }

    @Override
    public Poll viewPoll(int modId, String pollId) {
        Moderator mod = modRepository.findOne(modId); //dataStore.get(modId);
        return mod.getPoll(pollId);
    }

    @Override
    public Poll viewPollWithoutResult(String pollId) {
        Moderator mod=null;
        Poll returnPoll=null;
        List<Moderator> lstModerators = modRepository.findAll();
        /*Set<Map.Entry<Integer, Moderator>> entrySet = modRepository.findAll().  //dataStore.entrySet();
        for(Map.Entry entry: entrySet)
        {
            mod = (Moderator) entry.getValue();
            returnPoll = mod.getPoll(pollId);
            if(returnPoll.getId().equals(pollId))
            {
                break;
            }
        }*/
        Iterator<Moderator> itr = lstModerators.iterator();
        while(itr.hasNext())
        {
            mod = (Moderator) itr.next();
            returnPoll = mod.getPoll(pollId);
            if(returnPoll.getId().equals(pollId))
            {
                break;
            }
        }

        return returnPoll;
    }

    @Override
    public List<Poll> listPolls(int modId) {
        Moderator originalMod = modRepository.findOne(modId);  //dataStore.get(modId);
        Collection<Poll> map = originalMod.getAllPolls();
        List<Poll> lstPoll = new ArrayList<Poll>();
        Iterator<Poll> itrPoll = map.iterator();
        while(itrPoll.hasNext())
        {
            lstPoll.add(itrPoll.next());
        }
        return lstPoll;
    }

    @Override
    public void deletePoll(int modId, String pollId) {
        Moderator mod = modRepository.findOne(modId); //dataStore.get(modId);
        mod.deletePoll(pollId);
        modRepository.save(mod);
    }

    @Override
    public void voteAPoll(String pollId, int choice) {
        System.out.println("pollId "+pollId + " "+choice);
        Moderator mod=null;
        Poll returnPoll=null;
        List<Moderator> lstModerators = modRepository.findAll();
        Iterator<Moderator> itr = lstModerators.iterator();
        while(itr.hasNext())
        {
            mod = (Moderator) itr.next();
            returnPoll = mod.getPoll(pollId);
            if(returnPoll.getId().equals(pollId))
            {
                break;
            }
        }
        int[] val = returnPoll.getResults();
        if(null != val) {
            val[choice]++;
            returnPoll.setResults(val);
            mod.setPoll(pollId, returnPoll);
            modRepository.save(mod);//dataStore.put(mod.getId(), mod);
        }
    }

    public String generateAlphaNumAuthCode() {

        long range = 123456789L;
        Random r = new Random();
        long number = (long)(r.nextDouble()*range);
        String thirteenAsBase36 = Long.toString(number, 36);
        return thirteenAsBase36;

    }
}
