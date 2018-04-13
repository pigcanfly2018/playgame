package bsz.exch.handlers;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.product.bda.service.ConfigService;

import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.game.AginApi;
import bsz.exch.game.BbinApi;
import bsz.exch.game.KgApi;
import bsz.exch.game.MgApi;
import bsz.exch.game.PtApi;

@Handler(name="GAME")
public class GameHandler {
	
	/**
	 * 创建转账记录
	 * @param jdbcTemplate
	 * @param bill_no
	 * @param game_name
	 * @param game_pwd
	 * @param credit
	 * @param direct
	 * @param plat
	 * @return
	 */
	private Long createRecord(JdbcTemplate jdbcTemplate,String bill_no,String game_name,String game_pwd,String credit,String direct,String plat,String product){
		String sql="insert into game_transfer_data (bill_no,game_name,game_pwd,credit,direct,plat,create_date,flag,product) value(?,?,?,?,?,?,now(),0,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new MyPreparedStatementCreator(sql,new Object[]{bill_no,game_name,game_pwd,credit,direct,plat,product}), keyHolder);
		Long id= keyHolder.getKey().longValue();
		return id;
	}
	
	/**
	 * 完成转账记录
	 * @param jdbcTemplate
	 * @param data_id
	 * @param flag
	 * @return
	 */
	private boolean finishRecord(JdbcTemplate jdbcTemplate,Long data_id,int flag){
		String sql="update game_transfer_data set flag=?,finish_date=now() where data_id =? ";
		int c=jdbcTemplate.update(sql,new Object[]{flag,data_id});
		return c>0;
	}
	
	/**
	 * 查询游戏状态
	 * @param jdbcTemplate
	 * @param plat
	 * @param product
	 * @return
	 */

	
	/**
	 * 判断产品支持的平台
	 * @param task
	 * @param inter
	 * @return
	 */
	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String plat=task.getParam("plat");
		
		//检查产品
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		
		//检查是否支持的平台
		if(!("AG".equals(plat)||"BBIN".equals(plat)||"PT".equals(plat)||"KG".equals(plat)||"MG".equals(plat))){
			return  new FobbinInfo(true,plat+" is not support!");
		}
		
		//检查游戏是否关闭
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		boolean able=new ConfigService(template,inter.getDataSource()).queryGameStatus(plat,product);
		if(!able){
			return  new FobbinInfo(true,plat+" is closed");
		}
		
		return new FobbinInfo(false,"");
		
	}

	/**
	 * 查询额度
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="balance")
	@Params(validateField={"plat","loginname","password"})
	public Result balance(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");		
		String password=task.getParam("password");	
		String product=task.getParam("product");
		
		BigDecimal balance =new BigDecimal(0);
		if("AG".equals(plat)){
			 balance= AginApi.get(product).GetBalance(loginname, password);
		}
		if("BBIN".equals(plat)){
			 balance= BbinApi.get(product).GetBalance(loginname);
		}
		if("PT".equals(plat)){
			 balance= PtApi.get(product).GetBalance(loginname);
		}
		if("KG".equals(plat)){
			 balance= KgApi.get(product).GetBalance(loginname);
		}
		if("MG".equals(plat)){
			 balance= MgApi.get(product).GetBalance(loginname);
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(balance.toString());
		return result;
	}
	
	
	/**
	 * 转账
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="transfer")
	@Params(validateField={"plat","loginname","password","billno","type","credit","ip"})
	public Result transfer(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");
		String password =task.getParam("password");
		String billno =task.getParam("billno");
		String type =task.getParam("type");
		String credit =task.getParam("credit");
		String ip =task.getParam("ip");
		String product=task.getParam("product");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		Long data_id=createRecord(template, billno, loginname, password, credit, type, plat,product);
		boolean flag =false;
		if("AG".equals(plat)){
			flag= AginApi.get(product).Transfer(loginname, billno, type, new BigDecimal(credit), password);
		}
		if("BBIN".equals(plat)){
			flag= BbinApi.get(product).Transfer(loginname, billno, type, new BigDecimal(credit));
		}
		if("PT".equals(plat)){
			flag= PtApi.get(product).Transfer(loginname, billno, type, new BigDecimal(credit));
		}
		if("KG".equals(plat)){
			flag= KgApi.get(product).Transfer(loginname, billno, type, new BigDecimal(credit), ip);
		}
		if("MG".equals(plat)){
			flag= MgApi.get(product).Transfer(loginname, billno, type,  new BigDecimal(credit));
		}
		finishRecord(template, data_id, flag?1:2);


		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+flag);
		return result;
	}
	/**
	 * 创建账号
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="create")
	@Params(validateField={"plat","loginname","password","ip"})
	public Result create(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");
		String password =task.getParam("password");	
		String ip =task.getParam("ip");
		String product=task.getParam("product");
		
		boolean flag =false;
		if("AG".equals(plat)){
			flag= AginApi.get(product).CheckOrCreateGameAccount(loginname, password);
		}
		if("BBIN".equals(plat)){
			flag= BbinApi.get(product).CheckOrCreateGameAccount(loginname, password);
		}
		if("PT".equals(plat)){
			flag= PtApi.get(product).CheckOrCreateGameAccount(loginname, password);
		}
		if("KG".equals(plat)){
			String url=KgApi.get(product).CreateAccountAndLogin(loginname, ip);
			if(url!=null&&url.startsWith("/client")){
				flag=true;
			}
		}
		if("MG".equals(plat)){
			flag= MgApi.get(product).addAccount(loginname, password, ip);
		}
		
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+flag);
		return result;
	}
	
	
	
	/**
	 * 跳转
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="forward")
	@Params(validateField={"plat","loginname","password","ip"})
	public Result forward(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");
		String password =task.getParam("password");	
		String site =task.getParam("site");	
		String ip =task.getParam("ip");
		String product=task.getParam("product");
		
		String url ="";
		if("AG".equals(plat)){
			url= AginApi.get(product).ForwardGame(loginname, password,"");
		}
		if("BBIN".equals(plat)){
			url= BbinApi.get(product).ForwardGame(loginname, password, site);
		}
		if("PT".equals(plat)){
			//无实现方法
		}
		if("KG".equals(plat)){
			 url=KgApi.get(product).CreateAccountAndLogin(loginname, ip);
		}
		if("MG".equals(plat)){
			//无实现方法
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(url);
		return result;
	}
	
	/**
	 * 修改用户密码
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="update_pwd")
	@Params(validateField={"plat","loginname","password","ip"})
	public Result updatePwd(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");
		String password =task.getParam("password");		
		String ip =task.getParam("ip");
		String product=task.getParam("product");
		
		boolean flag =false;
		if("AG".equals(plat)){
			//无实现
		}
		if("BBIN".equals(plat)){
			//无实现
		}
		if("PT".equals(plat)){
			flag=PtApi.get(product).UpdatePwd(loginname, password);
		}
		if("KG".equals(plat)){
			//无实现
		}
		if("MG".equals(plat)){
			flag=MgApi.get(product).updatePassword(loginname, password);
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+flag);
		return result;
	}
	
	/**
	 * 修改客户资料
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="update_cust")
	@Params(validateField={"plat","loginname","password","ip"})
	public Result updateCust(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");	
		String ip =task.getParam("ip");
		String product=task.getParam("product");
		
		boolean flag =false;
		if("AG".equals(plat)){
			//无实现
		}
		if("BBIN".equals(plat)){
			//无实现
		}
		if("PT".equals(plat)){
			flag=PtApi.get(product).UpdateCustom02(loginname);
		}
		if("KG".equals(plat)){
			//无实现
		}
		if("MG".equals(plat)){
			//无实现
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+flag);
		return result;
	}
	
	/**
	 * 检查是否在线
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="check_online")
	@Params(validateField={"plat","loginname","ip"})
	public Result checkOnline(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");	
		String ip =task.getParam("ip");
		String product=task.getParam("product");
		
		
		boolean flag =false;
		if("AG".equals(plat)){
			//无实现
		}
		if("BBIN".equals(plat)){
			//无实现
		}
		if("PT".equals(plat)){
			flag=PtApi.get(product).CheckOnline(loginname);
		}
		if("KG".equals(plat)){
			//无实现
		}
		if("MG".equals(plat)){
			//无实现
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+flag);
		return result;
	}
	
	/**
	 * 将客户剔除
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="logout")
	@Params(validateField={"plat","loginname","ip"})
	public Result logout(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String loginname=task.getParam("loginname");	
		String ip =task.getParam("ip");
		String product=task.getParam("product");
		
		boolean flag =false;
		if("AG".equals(plat)){
			//无实现
		}
		if("BBIN".equals(plat)){
			//无实现
		}
		if("PT".equals(plat)){
			flag=PtApi.get(product).Logout(loginname);
		}
		if("KG".equals(plat)){
			//无实现
		}
		if("MG".equals(plat)){
			//无实现
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+flag);
		return result;
	}
	
	
	
}
