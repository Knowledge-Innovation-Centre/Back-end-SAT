/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.FileUpload;
import com.sat.entity.User;
import com.sat.entity.mapper.FileUploadExtractor;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author dean
 */
@Repository
public class FileUploadDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public int storeUploadedFile(final User currentUser, final String tablename, final Integer tablerecordid, final MultipartFile file) throws IOException{
        final String mimeType = file.getContentType();
        final String filename = file.getOriginalFilename();
        final byte[] bytes = file.getBytes();

        LobHandler lobHandler = new DefaultLobHandler(); 
        final LobCreator lobCreator=lobHandler.getLobCreator();

            
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql="INSERT INTO FILEUPLOAD(bytes,mime,filename,table_name,table_id,owner_username) VALUES (?,?,?,?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    lobCreator.setBlobAsBytes(stmt, 1, bytes);
                    stmt.setString(2, mimeType);
                    stmt.setString(3, filename);
                    if (tablename==null) {
                        stmt.setNull(4, Types.VARCHAR);
                        stmt.setNull(5, Types.INTEGER);
                    } else {
                        stmt.setString(4, tablename);
                        stmt.setInt(5, tablerecordid);
                    }
                    stmt.setString(6, currentUser.getUsername() );
                    
                    return stmt;
                }
            },
            keyHolder);

        int newId = keyHolder.getKey().intValue();
        return newId;
    }
    
    
    public FileUpload getUploadedFile(int id) {
        
        String sql = "select fu.id,fu.owner_username,fu.table_name,fu.table_id,fu.mime,fu.filename,fu.vrijeme,fu.bytes from FILEUPLOAD as fu where id = "+id;
        final LobHandler lobHandler = new DefaultLobHandler();
        
        List<FileUpload> files=jdbcTemplate.query(
                sql,  
                new RowMapper(){
                    @Override
                     public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        
                        FileUpload ff=new FileUpload();
                        byte[] requestData = lobHandler.getBlobAsBytes(rs, "bytes");
                        ff.setBytes(requestData);
                        ff.setMime_type(rs.getString("mime"));
                        ff.setFilename(rs.getString("filename"));
                        ff.setOwner_username(rs.getString("owner_username"));
                        ff.setTable_name(rs.getString("table_name"));
                        ff.setTable_id(rs.getInt("table_id"));
                        ff.setVrijeme(rs.getDate("vrijeme"));

                        return ff;
                    }
                }
        );
        if (files==null) return null;
        if (files.size()==0) return null;
        return files.get(0);
    }
    
    public List<FileUpload> findAll(User user, String school_id, String table_name, Integer table_id, Integer id) {
        
        if (user==null && school_id==null && table_name==null && id==null) return null;
        
        List<FileUpload> ret=new ArrayList<FileUpload>();
        
        ret = jdbcTemplate.query(" select  fu.id,fu.owner_username,fu.table_name,fu.table_id,fu.mime,fu.filename,fu.vrijeme "+
                " from FILEUPLOAD as fu JOIN USERS u on u.username=fu.owner_username  "+
                " where coalesce(?,fu.owner_username)=fu.owner_username "+
                "   AND coalesce(?,fu.table_name)=fu.table_name "+
                "   AND coalesce(?,fu.table_id)=fu.table_id "+
                "   AND coalesce(?,u.school_id)=u.school_id "+
                "   AND coalesce(?,fu.id)=fu.id ",
                new Object[] { (user==null?null:user.getUsername()),
                                table_name==null?null:table_name, 
                                table_id==null?null:table_id,
                                (user==null?null:user.getSchool()),
                                id==null?null:id
                }, 
                new FileUploadExtractor()
            );
        return ret;
    }
    
    public void deleteOwnFile(final User currentUser, final int file_id) {
        String sql0 = "DELETE from FILEUPLOAD where id=? and owner_username=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, file_id);
                    stmt.setString(2, currentUser.getUsername());
                }
            }
        );
    }
    
    public void deleteSchoolFile(final User currentUser, final int file_id) {
        //String sql0 = "delete from FILEUPLOAD where id=(select id from FILEUPLOAD as fu where fu.id=? and (select u.school_id from USERS u where u.username=fu.owner_username)=?)";
        String sql0=" DELETE FILEUPLOAD "+
                    " from FILEUPLOAD "+
                    " join USERS on owner_username=USERS.username "+
                    " where FILEUPLOAD.id=? and USERS.school_id=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, file_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
    }
    
    public void deleteTableFile(final User currentUser, final String tablename, final Integer table_id, final int file_id) {
        //String sql0 = "delete from FILEUPLOAD where id=(select id from FILEUPLOAD as fu where fu.id=? and (select u.school_id from USERS u where u.username=fu.owner_username)=?)";
        String sql0=" DELETE FILEUPLOAD "+
                    " from FILEUPLOAD "+
                    " join USERS on owner_username=USERS.username "+
                    " where FILEUPLOAD.id=? and USERS.school_id=? and FILEUPLOAD.table_name=? and FILEUPLOAD.table_id=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, file_id);
                    stmt.setString(2, currentUser.getSchool());
                    stmt.setString(3, tablename);
                    stmt.setInt(4, table_id);
                    
                }
            }
        );
    }
    
}
