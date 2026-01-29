package org.springblade.modules.sp.controller;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.sp.dto.UserDTO;
import org.springblade.modules.sp.entity.*;
import org.springblade.modules.sp.service.*;
import org.springblade.modules.sp.vo.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.github.pagehelper.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sp/subscription")
@Api(value = "订阅配置控制器", tags = "订阅配置接口")
public class SubscriptionConfigController {

    @Autowired
    private SubscriptionConfigService subscriptionConfigService;
    @Autowired
    private SubscriptionOrderService subscriptionOrderService;
    @Autowired
    private SubscriptionOrderStreamsService subscriptionOrderStreamsService;
    @Autowired
    private StreamAclService streamAclService;
    @Autowired
    private StreamService streamService;
    @Autowired
    private IUsersService usersService;

    //    ---------------订阅配置控制器---------------
    @SneakyThrows
    @GetMapping("/selectSubscriptionConfigList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "selectSubscriptionConfigList", notes = "selectSubscriptionConfigList")
    public R<PageSubVO> selectSubscriptionConfigList(
            @RequestParam Optional<String> name,
            @RequestParam Optional<Integer> status,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Optional<Date> createTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Optional<Date> onlineTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Optional<Date> offlineTime,
            @RequestParam Optional<Integer> pageSize,
            @RequestParam Optional<Integer> currentPage) {
        try {
            PageSubVO pageSubVO = new PageSubVO();
            Page<SubscriptionConfig> subscriptionConfigs = subscriptionConfigService.selectSubscriptionConfigList(
                    name.orElse(null),
                    status.orElse(null),
                    createTime.orElse(null),
                    onlineTime.orElse(null),
                    offlineTime.orElse(null),
                    pageSize.orElse(null), currentPage.orElse(null));
            pageSubVO.setPage(subscriptionConfigs);
            pageSubVO.setTotal(subscriptionConfigs.getTotal());
            return R.data(pageSubVO);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/selectSubscriptionConfig.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "selectSubscriptionConfig", notes = "selectSubscriptionConfig")
    public R<SubscriptionConfig> selectSubscriptionConfig(@RequestParam String id) {
        try {
            SubscriptionConfig subscriptionConfig = subscriptionConfigService.getById(id);
            return R.data(subscriptionConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addSubscriptionConfig.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addSubscriptionConfig", notes = "addSubscriptionConfig")
    public R<String> addSubscriptionConfig(@RequestBody SubscriptionConfig subscriptionConfig) {
        try {
            subscriptionConfig.setOnlineTime(null);
            subscriptionConfig.setOfflineTime(null);
            subscriptionConfig.setStatus(0);
            subscriptionConfigService.insertSubscriptionConfig(subscriptionConfig);
            return R.data(subscriptionConfig.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateSubscriptionConfig.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateSubscriptionConfig", notes = "updateSubscriptionConfig")
    public R<String> updateSubscriptionConfig(@RequestBody SubscriptionConfig subscriptionConfig) {
        try {
            subscriptionConfigService.updateSubscriptionConfig(subscriptionConfig);
            return R.data(subscriptionConfig.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteSubscriptionConfig.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteSubscriptionConfig", notes = "deleteSubscriptionConfig")
    public R<String> deleteSubscriptionConfig(@RequestParam String id) {
        try {
            subscriptionConfigService.deleteSubscriptionConfig(id);
            return R.data(id);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/online.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "online", notes = "online")
    public R<String> online(@RequestParam String id) {
        try {
            SubscriptionConfig config = subscriptionConfigService.getById(id);
            config.setOnlineTime(new Date());
            config.setStatus(1);
            boolean b = subscriptionConfigService.updateSubscriptionConfig(config);
            if (b) {
                return R.data("上线成功");
            } else {
                return R.fail("上线失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/offline.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "offline", notes = "offline")
    public R<String> offline(@RequestParam String id) {
        try {
            SubscriptionConfig config = subscriptionConfigService.getById(id);
            config.setOfflineTime(new Date());
            config.setStatus(0);
            boolean b = subscriptionConfigService.updateSubscriptionConfig(config);
            if (b) {
                return R.data("下线成功");
            } else {
                return R.fail("下线失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    ---------------订阅管理控制器---------------

    @GetMapping("/selectSubscriptionOrderList.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "selectSubscriptionOrderList", notes = "selectSubscriptionOrderList")
    public R<PageSubOrderVO> selectSubscriptionOrderList(@RequestParam Optional<String> userId,
                                                         @RequestParam Optional<Integer> status,
                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Optional<Date> startTime,
                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Optional<Date> endTime,
                                                         @RequestParam Optional<Integer> pageSize,
                                                         @RequestParam Optional<Integer> currentPage) {
        try {
            PageSubOrderVO pageSubVO = new PageSubOrderVO();
            Page<SubscriptionOrderDetailVO> subscriptionOrders = subscriptionOrderService.selectSubscriptionOrderList(
                    userId.orElse(null),
                    status.orElse(null),
                    startTime.orElse(null),
                    endTime.orElse(null),
                    pageSize.orElse(null),
                    currentPage.orElse(null));
            pageSubVO.setPage(subscriptionOrders);
            pageSubVO.setTotal(subscriptionOrders.getTotal());
            return R.data(pageSubVO);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/selectSubscriptionOrder.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "selectSubscriptionOrder", notes = "selectSubscriptionOrder")
    public R<SubscriptionOrder> selectSubscriptionOrder(@RequestParam String id) {
        try {
            SubscriptionOrder subscriptionOrder = subscriptionOrderService.getById(id);
            return R.data(subscriptionOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/addSubscriptionOrder.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addSubscriptionOrder", notes = "addSubscriptionOrder")
    public R<String> addSubscriptionOrder(@RequestBody SubOrderVO subOrderVO) {
        try {
            // 获取订阅配置信息
            SubscriptionConfig config = subscriptionConfigService.getById(subOrderVO.getConfigId());
            if (config == null) {
                return R.fail("订阅配置不存在");
            }

            SubscriptionOrder subscriptionOrder = new SubscriptionOrder();
            subscriptionOrder.setConfigId(subOrderVO.getConfigId());
            subscriptionOrder.setUserId(subOrderVO.getUserId());
            subscriptionOrder.setOrderTime(subOrderVO.getOrderTime());
            String unit = config.getStorageUnit();
            //设置初始套餐容量
            // 计算总容量（转换为KB）
            long totalStorageKB;
            switch (unit.toUpperCase()) {
                case "G":
                    totalStorageKB = config.getStorage() * 1024 * 1024L;
                    break;
                case "M":
                    totalStorageKB = config.getStorage() * 1024L;
                    break;
                case "T":
                    totalStorageKB = config.getStorage() *1024 * 1024 * 1024L;
                    break;
                default:
                    return R.fail("存储单位配置错误");
            }

            subscriptionOrder.setTotalStorage(totalStorageKB);


            // 计算已使用时长（当前时间 - 订阅时间）
            long currentTimeMillis = System.currentTimeMillis();
            long orderTimeMillis = subOrderVO.getOrderTime().getTime();
            long usedDuration = (currentTimeMillis - orderTimeMillis) / (1000 * 60 * 60 * 24); // 转换为天数
            subscriptionOrder.setUsedDuration((int) usedDuration);

            // 计算到期时间：订阅时间 + 使用期限(月)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(subOrderVO.getOrderTime());
            calendar.add(Calendar.MONTH, config.getDuration());
            subscriptionOrder.setDeadTime(calendar.getTime());

            subscriptionOrder.setUsedProjectNum(1);
            subscriptionOrder.setUsedUserNum(1);
            subscriptionOrder.setUsedStorage(0L);
            subscriptionOrder.setStatus(0);

            boolean b = subscriptionOrderService.insertSubscriptionOrder(subscriptionOrder);

            if (!b) {
                return R.fail("添加失败");
            }

//            初始化项目流量表
            List<String> resourceIdsByUserId = streamAclService.getResourceIdsByUserId(subOrderVO.getUserId());
            for (String streamId : resourceIdsByUserId) {
                SubscriptionOrderStreams subscriptionOrderStreams = new SubscriptionOrderStreams();
                List<StreamAcl> streamAcls = streamAclService.selectStreamAclByStreamId(streamId);
                for (StreamAcl streamAcl : streamAcls) {
                    if (streamAcl.getRole().equals("stream:owner")){
                        subscriptionOrderStreams.setOwner(usersService.getById(streamAcl.getUserId()).getName());
                    }
                }
                subscriptionOrderStreams.setStreamId(streamId);
                subscriptionOrderStreams.setOrderId(subscriptionOrder.getId());
                subscriptionOrderStreams.setName(streamService.getStreamNameById(streamId));
                subscriptionOrderStreams.setUsedStorage(0L);
                subscriptionOrderStreams.setTotalStorage(totalStorageKB);
                subscriptionOrderStreams.setProjectNum(config.getProjectNum());
                subscriptionOrderStreams.setUserNum(resourceIdsByUserId.size());
                subscriptionOrderStreams.setDeadTime(calendar.getTime());
                subscriptionOrderStreamsService.addSubscriptionOrderStreams(subscriptionOrderStreams);
            }
            return R.data(subscriptionOrder.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateSubscriptionOrder.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateSubscriptionOrder", notes = "updateSubscriptionOrder")
    public R<String> updateSubscriptionOrder(@RequestBody SubscriptionOrder subscriptionOrder) {
        try {
            subscriptionOrderService.updateSubscriptionOrder(subscriptionOrder);
            return R.data(subscriptionOrder.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateSubscriptionOrderStatus.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateSubscriptionOrderStatus", notes = "updateSubscriptionOrderStatus")
    public R<String> updateSubscriptionOrderStatus(@RequestParam String id, @RequestParam Integer status) {
        try {
            subscriptionOrderService.updateSubscriptionOrderStatus(id, status);
            return R.data(id);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/deleteSubscriptionOrder.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteSubscriptionOrder", notes = "deleteSubscriptionOrder")
    public R<String> deleteSubscriptionOrder(@RequestParam String id) {
        try {
            subscriptionOrderService.deleteSubscriptionOrder(id);
            return R.data(id);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    总空间扩容
    @SneakyThrows
    @PostMapping("/expandTotalStorage.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "expandTotalStorage", notes = "expandTotalStorage")
    public R<String> expandTotalStorage(@RequestParam String id, @RequestParam Integer storage) {
        try {
            SubscriptionOrder order = subscriptionOrderService.getById(id);
            if (order == null) {
                return R.fail("订阅不存在");
            }
            String unit = subscriptionConfigService.getById(order.getConfigId()).getStorageUnit();
            long addStrorage;
            switch (unit.toUpperCase()) {
                case "G":
                    addStrorage = storage * 1024 * 1024L;
                    break;
                case "M":
                    addStrorage = storage * 1024L;
                    break;
                case "T":
                    addStrorage = storage *1024 * 1024 * 1024L;
                    break;
                default:
                    return R.fail("存储单位配置错误");
            }
            Long totalStorage = order.getTotalStorage() + addStrorage;
            boolean b = subscriptionOrderService.expandTotalStorage(id, totalStorage);
            if (!b) {
                return R.fail("扩容失败");
            }
            return R.data("扩容成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    订阅续期
    @SneakyThrows
    @PostMapping("/renewSubscriptionOrder.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "renewSubscriptionOrder", notes = "renewSubscriptionOrder")
    public R<String> renewSubscriptionOrder(@RequestParam String id,@RequestParam Integer month) {
        try {
            // 获取订阅订单信息
            SubscriptionOrder order = subscriptionOrderService.getById(id);
            if (order == null) {
                return R.fail("订阅订单不存在");
            }

            // 获取原到期时间
            Date oldDeadTime = order.getDeadTime();
            if (oldDeadTime == null) {
                return R.fail("订单到期时间异常");
            }

            // 计算新的到期时间：原到期时间 + (月数 * 30天)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(oldDeadTime);
            calendar.add(Calendar.DAY_OF_MONTH, month * 30);

            // 更新订单到期时间
            order.setDeadTime(calendar.getTime());

            // 更新订单信息
            boolean updated = subscriptionOrderService.updateSubscriptionOrder(order);
            if (!updated) {
                return R.fail("续期失败");
            }
            return R.data("续期成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("续期失败：" + e.getMessage());
        }
    }
//    项目流量表管理

    @SneakyThrows
    @GetMapping("/getSubscriptionOrderStreams.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getSubscriptionOrderStreams", notes = "getSubscriptionOrderStreams")
    public R<List<SubscriptionOrderStreams>> getSubscriptionOrderStreams(@RequestHeader("Authorization") String token) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }

            // 获取用户订阅信息
            SubscriptionOrder order = subscriptionOrderService.getByUserId(user.getId());
            if (order == null) {
                return R.fail("用户未订阅");
            }
            List<SubscriptionOrderStreams> subscriptionOrderStreams = subscriptionOrderStreamsService.getByOrderId(order.getId());
            return R.data(subscriptionOrderStreams);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

//    获取UserOrderDetailVO

    @SneakyThrows
    @PostMapping("/getUserOrderDetailVO.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getUserOrderDetailVO", notes = "getUserOrderDetailVO")
    public R<UserOrderDetailVO> getUserOrderDetailVO(@RequestHeader("Authorization") String token) {
        try {
            // 通过token获取用户信息
            Users user = usersService.getUserByToken(token);
            if (user == null) {
                return R.fail("用户未登录或token已过期");
            }

            // 获取用户订阅信息
            SubscriptionOrder order = subscriptionOrderService.getByUserId(user.getId());
            if (order == null) {
                return R.fail("用户未订阅");
            }

            SubscriptionConfig config = subscriptionConfigService.getById(order.getConfigId());
            UserOrderDetailVO userOrderDetailVO = new UserOrderDetailVO();
            userOrderDetailVO.setSubscriptionConfig(config);
            userOrderDetailVO.setDeadTime(order.getDeadTime());
            userOrderDetailVO.setUsedProjectNum(order.getUsedProjectNum());
            userOrderDetailVO.setUsedStorage(order.getUsedStorage());
            userOrderDetailVO.setTotalProjectNum(config.getProjectNum());
            userOrderDetailVO.setTotalStorage(order.getTotalStorage());
            return R.data(userOrderDetailVO);

        }catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }

    }

//    用户管理

    @SneakyThrows
    @PostMapping("/addUser.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "addUser", notes = "addUser")
    public R<String> addUser(@RequestParam String name, @RequestParam String password,@RequestParam String email,@RequestParam String phone,@RequestParam String challenge,@RequestParam Integer isOrder) {
        try {
            String registerRes = usersService.spRegister(email, name, password, "", phone, challenge).getData();
            if (registerRes == null) {
                return R.fail("新增用户失败");
            }
            Users user = usersService.getUserByPhone(phone);
            R<String> stringR = this.updateIsOrder(user.getId(), isOrder);
            if (stringR.getData().equals("修改成功")){
                return R.data(registerRes);
            }else {
                return R.fail("新增用户失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    //TODO 用户关联的stream/commits/模版

    @SneakyThrows
    @PostMapping("/deleteUser.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "deleteUser", notes = "deleteUser")
    public R<String> deleteUser(@RequestBody UserDTO user) {
        try {
            boolean b = usersService.batchDeleteSubscriptionUsers(user.getIds());
            if (b) {
                return R.data("删除成功");
            } else {
                return R.fail("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateSubscriptionUser.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateSubscriptionUser", notes = "updateSubscriptionUser")
    public R<String> updateSubscriptionUser(@RequestBody Users user) {
        try {
            boolean b = usersService.updateSubscriptionUser(user);
            if (b) {
                return R.data("修改成功");
            } else {
                return R.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @PostMapping("/updateIsOrder.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "updateIsOrder", notes = "updateIsOrder")
    public R<String> updateIsOrder(@RequestParam String id, @RequestParam Integer isOrder) {
        try {
            boolean b = usersService.updateIsOrder(id, isOrder);
            if (b) {
                return R.data("修改成功");
            } else {
                return R.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/filterSubscriptionUsers.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页查询订阅用户", notes = "分页查询订阅用户")
    public R<IPage<SubscriptionUserVO>> filterSubscriptionUsers(
            @RequestParam Optional<String> filterInput,
            @RequestParam Optional<Integer> isOrder,
            @RequestParam Optional<String> startTime,
            @RequestParam Optional<String> endTime,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer currentPage) {
        try {
            if (pageSize <= 0 || currentPage <= 0) {
                return R.fail("分页参数错误");
            }

            IPage<SubscriptionUserVO> page = usersService.filterSubscriptionUsers(
                    filterInput.orElse(null),
                    isOrder.orElse(null),
                    startTime.orElse(null),
                    endTime.orElse(null),
                    pageSize,
                    currentPage
            );

            return R.data(page);
        } catch (Exception e) {
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @SneakyThrows
    @GetMapping("/getSubscriptionUser.do")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "getSubscriptionUser", notes = "getSubscriptionUser")
    public R<SubscriptionUserVO> getSubscriptionUser(@RequestParam String id) {
        try {
            SubscriptionUserVO subscriptionUser = usersService.getSubscriptionUserById(id);
            return R.data(subscriptionUser);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


}