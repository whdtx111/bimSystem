package org.springblade.modules.sp.constant;

public interface SpConstant {

    public static final  String STRING_DEFAULT_VALUE_0 = "0";
    public static final  String STRING_DEFAULT_VALUE_1 = "1";
    public static final  int INT_DEFAULT_VALUE_1 = 1;
    public static final  int INT_DEFAULT_VALUE_0 = 0;
    public static final  String WBS_CODE_1 = "-";
    public static final  String WBS_CODE_2 = ".";
    public static final String MODEL = "model";
    public static final int INFOTYPE_1 = 1;
    public static final  int INFOTYPE_2 = 2;
    public static final String GOUJIAN = "构件";
    public static final  String OBJTYPE = "objectsType";
    public static final  String STREAMSTYPE = "streamsType";
    /**
     * sp 校验盐
     */
    public static final String APP_SECRET = "smartlink_secret";
    public static final String APP_ID = "smartlink_id";
    // 固定盐返回
    public static final String STR_MSG = "盐校验不通过!";
    /**
     * 管理员角色
     * */
    public static final String ADMIN_ROLE = "server:admin";
    /**
     * 用户角色
     * */
    public static final String USER_ROLE = "server:user";

    /**
     * 同步消息
     * */
    public static final String SYNC_SUCCESS = "同步成功";
    /**
     * 同步失败
     * */
    public static final String SYNC_FAIL = "同步失败";
    /**
     * 用户公司标记
     * */
    public static final String COMPANY_FLAG=  "ZXGJ";
    /**
     * 邮箱后缀
     * */
    public static final String SP_EMAIL_SUFFIX = "@smartlink.com";

    /**
     * 初始化密码
     * */
    public static final String INIT_PASSWORD = "ZXGJ123456";

    /**
     * sm原生登录换 code地址
     */
//    public static final String AUTH_LOCAL_LOGIN_URL = "10.5.58.132:30071/auth/local/login?challenge=";
    /**
     * sm原生登 地址
     */
//    public static final String TOKEN_URL = "10.5.58.132:30071/auth/token";

    /**
     * sm原注册 地址
     */
//    public static final String REGISTER_URL = "http://10.5.58.132:30071/auth/local/register";

    /**
     * sm appid appsecret
     */
    public static final String APPID_SPKLWEBAPP = "spklwebapp";
    public static final String APPSECRET_SPKLWEBAPP = "spklwebapp";
    public static final String APPID_PLUGIN = "sca";
    public static final String APPSECRET_PLUGIN = "sca";

    /**
     * 账号不存在
     * */
    public static final String ACCOUNT_ERROR = "无效的凭证。";

    /**
     * 访问码不存在
     * */
    public static final String TOKEN_CODE_ERROR = "无效的访问码。";

    public static final String BASE_HEADER [] = {"id","streamId","category","elementId"};

    public static final String SPECKLE_TYPE_BUILTELEMENTS = "Objects.BuiltElements.Revit.RevitElementType:Objects.BuiltElements.Revit.RevitSymbolElementType";
    public static final String SPECKLE_TYPE_OTHER = "Objects.Other.Revit.RevitInstance:Objects.BuiltElements.Revit.RevitMEPFamilyInstance";
    public static final String SPECKLE_TYPE_OTHER_2 = "Objects.Other.Revit.RevitInstance";

    public static final String CATEGORY = "category";
    public static final String FAMILY = "family";
    public static final String TYPE = "type";
    public static final String FAMILY_NAME = "名称";
    public static final String TYPE_NAME = "类型";
    public static final String ELEMENTID = "elementId";
    public static final String ELEMENTTYPE = "elementType";
    public static final String ELEMENTINSTANCE = "elementInstance";
    public static final String REMOVESIGN = "del";
    public static final String REMOVE_XIANGJI = "相机";
    public static final String REMOVE_CAIZHI = "材质";
    public static final String REMOVE_SHITU = "视图";
    public static final String SP_REDIS_TREE_KEY = "tree_list_";
    public static final String SP_REDIS_OBJ_KEY = "obj_list_";
    public static final String SP_REDIS_OBJ_PARAMETER_KEY = "obj_parameter_";
    public static final String SP_REDIS_OBJ_PARAMETER_NAME_KEY = "obj_parameter_name_";

    public static final long REDIS_TIME_2880 = 2880L;
    public static final long REDIS_TIME_120 = 120L;




}
