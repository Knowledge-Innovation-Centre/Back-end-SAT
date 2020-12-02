/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.FileUpload;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author dean
 */
public class FileUploadExtractor implements ResultSetExtractor<List<FileUpload>>  {

    @Override
    public List<FileUpload> extractData(ResultSet rs) throws SQLException, DataAccessException {
        
        List<FileUpload> ret=new ArrayList<FileUpload>();
                
        while (rs.next()){
            // fu.id,fu.owner_username,fu.table_name,fu.table_id,fu.mime,fu.filename,fu.vrijeme
            FileUpload fu=new FileUpload();
            fu.setId(rs.getInt("id"));
            fu.setOwner_username(rs.getString("owner_username"));
            fu.setTable_name(rs.getString("table_name"));
            fu.setTable_id(rs.getInt("table_id"));
            fu.setMime_type(rs.getString("mime"));
            fu.setFilename(rs.getString("filename"));
            fu.setVrijeme(rs.getDate("vrijeme"));
            ret.add(fu);
        }
        
        return ret;
    }
    
}
