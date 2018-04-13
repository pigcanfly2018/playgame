package jobs;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import models.Msg;
import models.UserMsg;
import play.jobs.Every;
import play.jobs.Job;
import util.Sp;

/**
 * 扫描msg并发送到感兴趣的人身上
 * @author lance
 *
 */
@Every("10s")
public class MsgJob  extends Job{
	public void doJob()throws Exception {
//	      Sp.get().getDefaultJdbc()
//			.execute("{call mb_process_msg()}", new CallableStatementCallback() {
//				public String doInCallableStatement(CallableStatement cs)
//						throws SQLException, DataAccessException {
//					cs.executeUpdate();
//                     return "";
//				}
//			});

	}
}
