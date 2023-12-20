package com.example.web_test.mapper;

import com.example.web_test.pojo.Evaluation;
import com.example.web_test.pojo.EvaluationMember;
import com.example.web_test.pojo.EvaluationStat;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EvaluationMapper {

    @Select("select * from evaluation where hwID=#{hwID}")
    Evaluation getEvaluation(int hwID);

    @Update("update homework set isStartEva=true where hwID=#{hwID}")
    void startEva(int hwID);

    @Update("update evaluation set ddl=#{ddl} where hwID=#{hwID}")
    void setEvaDdl(int hwID, LocalDateTime ddl);

    @Insert("insert into evaluation(hwID, ddl) VALUES (#{hwID}, #{ddl})")
    void addEva(int hwID, LocalDateTime ddl);

    @Insert("insert into evaluationstat(uID, hwID) VALUES (#{uid}, #{hwID})")
    void createEva(int uid, int hwID);

    @Select("select * from evaluationstat where uID=#{uID} and hwID=#{hwID}")
    EvaluationStat getMyEvaluation(int uID, int hwID);

    @Select("select * from evaluationstat where hwID=#{hwID}")
    List<EvaluationStat> listEvaluations(int hwID);

    @Select("select * from evaluation_member where uID=#{uID} and desID=#{desID} and hwID=#{hwID}")
    List<EvaluationMember> getEvaluationMember(int uID, int desID, int hwID);

    @Options(keyProperty = "eID", useGeneratedKeys = true)
    @Insert("insert into evaluation_member(uID, hwID, desID) values (#{uID}, #{hwID}, #{desID})")
    void createEvaluationMember(EvaluationMember evaluationMember);

    @Select("select * from evaluation_member where eID=#{eID} and uID=#{uID}")
    EvaluationMember getEvaluationMember2(int eID, int uID);

    @Update("update evaluation_member set score=#{score}, comment=#{comment}, image=#{image} where eID=#{eID}")
    void evaluate(EvaluationMember evaluationMember);

    @Select("select * from evaluation_member where uID=#{uID} and hwID=#{hwID} and (score is null)")
    List<EvaluationMember> getNoEvaluation(int uID, int hwID);

    @Select("select * from evaluation_member where desID=#{desID} and hwID=#{hwID} and score is not null")
    List<EvaluationMember> getEvaluationResult(int desID, int hwID);

    @Update("update evaluationstat set score=#{score} where uID=#{uid} and hwID=#{hwID}")
    void setScore(int uid, int score, int hwID);

    @Select("select * from evaluationstat where hwID=#{hwID} order by score desc")
    List<EvaluationStat> getRank(int hwID);

    @Update("update evaluationstat set evaluateNum=evaluateNum+1 where uID=#{uid} and hwID=#{hwID}")
    void addEvaluateNum(int uid, int hwID);

    @Update("update evaluationstat set evaluatedNum=evaluatedNum+1 where uID=#{desID} and hwID=#{hwID}")
    void addEvaluatedNum(int desID, int hwID);

    @Select("select * from evaluation_member where hwID=#{hwID} and desID=#{sID}")
    List<EvaluationMember> getEvaluatedMember(int hwID, int sID);

    @Select("select * from evaluation_member where eID=#{eID}")
    EvaluationMember getEvaluationMember3(int eID);

    @Update("update evaluationstat set score=#{score} where uID=#{uID} and hwID=#{hwID}")
    void modifyScore(int uID, int hwID, int score);
}
