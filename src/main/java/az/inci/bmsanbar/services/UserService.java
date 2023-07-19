/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.services;

import az.inci.bmsanbar.model.User;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author User
 */
@SuppressWarnings({"unchecked", "SqlNoDataSourceInspection", "SqlResolve"})
@Service
public class UserService extends AbstractService
{


    public List<User> all()
    {
        Query q = em.createNativeQuery("SELECT USER_ID, USER_NAME FROM BMS_USER");
        List<Object[]> resultList = q.getResultList();
        List<User> userList = new ArrayList<>();
        resultList.stream().map((object)->
                                {
                                    User user = new User();
                                    user.setId(String.valueOf(object[0]));
                                    user.setName(String.valueOf(object[1]));
                                    return user;
                                }).forEachOrdered(userList::add);
        em.close();

        return userList;
    }

    public User getById(String id)
    {
        Query q = em.createNativeQuery("exec dbo.SP_USER_INFO_FOR_PICK ?");
        q.setParameter(1, id);
        User user = new User();
        String field;
        boolean value;
        List<Object[]> resultList = q.getResultList();
        if(resultList.size() > 0)
        {
            user.setId(String.valueOf(resultList.get(0)[0]));
            user.setName(String.valueOf(resultList.get(0)[1]));
            user.setPassword(String.valueOf(resultList.get(0)[2]));
            user.setPickGroup(String.valueOf(resultList.get(0)[6]));
            user.setWhsCode(String.valueOf(resultList.get(0)[7]));
        }
        for(Object[] result : resultList)
        {
            field = String.valueOf(result[3]);
            value = Boolean.parseBoolean(String.valueOf(result[4]));
            user.setUserInfo(field, value);
        }
        em.close();
        return user;
    }

    public boolean login(String id, String password)
    {
        Query q = em.createNativeQuery(
                "select top 1 USER_ID, USER_NAME, PASS_WORD from BMS_USER where USER_ID=?");
        q.setParameter(1, id);
        User user = new User();
        List<Object[]> result = q.getResultList();

        if(result.size() == 1)
        {
            user.setId(String.valueOf(result.get(0)[0]));
            user.setName(String.valueOf(result.get(0)[1]));
            user.setPassword(String.valueOf(result.get(0)[2]));
            return password.equals(user.getPassword());
        }

        em.close();

        return false;
    }
}
