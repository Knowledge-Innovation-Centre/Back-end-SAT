/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.IdHolder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Dean
 */
public class IdHolderExtractor  implements ResultSetExtractor<List<IdHolder>>{

    @Override
    public List<IdHolder> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<IdHolder> ret=new ArrayList<IdHolder>();
        
        int id;
        while (rs.next()){
            id=rs.getInt("id");
            IdHolder r=new IdHolder();
            r.setId(id);
            ret.add(r);
        }
        return ret;
    }
    
}
