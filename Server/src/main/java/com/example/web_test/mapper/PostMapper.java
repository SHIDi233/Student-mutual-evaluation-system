package com.example.web_test.mapper;

import com.example.web_test.pojo.Post;
import com.example.web_test.pojo.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Options(keyProperty = "pID", useGeneratedKeys = true)
    @Insert("insert into post(pName, cID, uID, content, teacherOnly) VALUES(#{pName}, #{cID}, #{uID}, #{content}, #{teacherOnly})")
    public void createPost(Post post);

    @Select("select * from post where cID=#{cID} order by cTime desc")
    List<Post> getPosts(int cID);

    @Select("select numOfReply from post where pID=#{pID}")
    Integer getFloor(int pID);

    @Update("update post set numOfReply=numOfReply+1 where pID=#{pID}")
    void addFloor(int pID);

    @Insert("insert into reply_of_post(uID, pID, content, floor) VALUES(#{uID}, #{pID}, #{content}, #{floor})")
    void reply(int uID, int pID, String content, int floor);

    @Select("select * from post where pID=#{pID}")
    Post getPost(int pID);

    @Select("select * from reply_of_post where pID=#{pID} order by floor")
    List<Reply> getReplies(int pID);
}
