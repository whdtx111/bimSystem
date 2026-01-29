package org.springblade.modules.sp.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.sp.entity.NewTemplate;
import org.springblade.modules.sp.mapper.NewTemplateMapper;
import org.springblade.modules.sp.service.NewTemplateService;
import org.springblade.modules.sp.service.WhiteListService;
import org.springblade.modules.sp.vo.TemplateStreamVO;
import org.springblade.modules.sp.vo.StreamListTemplatesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 新模板服务实现类
 */
@Slf4j
@Service
@DS("postgresql")
public class NewTemplateServiceImpl extends ServiceImpl<NewTemplateMapper, NewTemplate> implements NewTemplateService {
    @Autowired
    NewTemplateMapper newTemplateMapper;
    
    @Autowired
    private WhiteListService whiteListService;

    @Override
    public List<NewTemplate> getTemplateList() {
        return newTemplateMapper.getTemplateList();
    }

    @Override
    public NewTemplate getTemplateById(String id) {
        try {
            return newTemplateMapper.getTemplateById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<NewTemplate> getTemplateListByStreamId(String streamId,String branchId) {
        try {
            return newTemplateMapper.getTemplateListByStreamId(streamId,branchId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<NewTemplate> getTemplateListDefault() {
        try {
            return newTemplateMapper.getTemplateListDefault();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<NewTemplate> getNewTemplateByStreamListId(String streamListId) {
        try {
            return newTemplateMapper.getNewTemplateByStreamListId(streamListId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<NewTemplate> getTemplateListByPid(String pid) {
        try {
            return newTemplateMapper.getTemplateListByPid(pid);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<StreamListTemplatesVO> getTemplateWithStreamByStreamId() {
        try {
            List<StreamListTemplatesVO> result = newTemplateMapper.getAllStreamListTemplates();
            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

   @Override
   public List<NewTemplate> getBomTemplate(String streamId,String branchId) {
        try {
            return newTemplateMapper.getBomTemplate(streamId,branchId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
   }

    @Override
    public List<NewTemplate> filterTemplateList(String pid,String fileId,String streamId,String branchId,String name,String type,String source,String version,String detail,String modifyUser,String auth) {
        try {
            return newTemplateMapper.filterTemplateList(pid,fileId,streamId,branchId,name,type,source,version,detail,modifyUser,auth);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<StreamListTemplatesVO> getStreamListTemplatesById(String streamListId) {
        try {
            List<StreamListTemplatesVO> result = newTemplateMapper.getStreamListTemplatesById(streamListId);
            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTemplate(NewTemplate newTemplate) {
       try {
           // 设置创建时间和修改时间
           newTemplate.setModifyTime(new Date());
           // 设置状态为启用
           if (newTemplate.getStatus() == null) {
               newTemplate.setStatus(0);
           }
           return newTemplateMapper.addTemplate(newTemplate);
       }catch (Exception e) {
           e.printStackTrace();
           return false;
       }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplate(NewTemplate newTemplate) {
      try {
          if (newTemplate == null || newTemplate.getId() == null) {
              return false;
          }
          // 设置修改时间
          newTemplate.setModifyTime(new Date());
          return newTemplateMapper.updateTemplate(newTemplate);
      }catch (Exception e) {
          e.printStackTrace();
          return false;
      }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTemplate(String id) {
      try {
          if (Func.isEmpty(id)) {
              return false;
          }
          
          // 1. 递归删除子模板
          List<NewTemplate> templateListByPid = newTemplateMapper.getTemplateListByPid(id);
          if (templateListByPid != null && templateListByPid.size() > 0) {
              for (NewTemplate newTemplate : templateListByPid) {
                  // 递归调用，会级联删除子模板的白名单
                  deleteTemplate(newTemplate.getId());
              }
          }
          
          // 2. 删除该模板关联的白名单记录
          try {
              whiteListService.deleteByTemplateId(id);
              log.info("删除模板的白名单记录成功, templateId={}", id);
          } catch (Exception e) {
              log.warn("删除模板的白名单记录失败, templateId={}, error={}", id, e.getMessage());
              // 不影响主流程，继续删除模板
          }
          
          // 3. 删除模板本身
          return newTemplateMapper.deleteTemplate(id) ;
      }catch (Exception e) {
          e.printStackTrace();
          return false;
      }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByStream(String streamId) {
      try {
          return newTemplateMapper.deleteByStream(streamId);
      }catch (Exception e) {
          e.printStackTrace();
          return false;
      }
    }


    @Override
    public List<NewTemplate> getTemplatesByStreamListIdWithNullStreamId(String streamListId) {
        try {
            System.out.println("查询streamListId=" + streamListId + " 下的所有streamId为null/空的模板");
            List<NewTemplate> templates = newTemplateMapper.getTemplatesByStreamListIdWithNullStreamId(streamListId);
            System.out.println("查询结果: 找到 " + templates.size() + " 个模板");
            for (NewTemplate template : templates) {
                System.out.println("- ID: " + template.getId() + ", Name: " + template.getName() + 
                    ", StreamId: [" + template.getStreamId() + "], PID: " + template.getPid());
            }
            return templates;
        } catch (Exception e) {
            System.out.println("查询模板失败: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCopyTemplatesWithRelations(List<NewTemplate> templatesToCopy, String newStreamId, String streamListId) {
        try {
            System.out.println("=== 开始批量复制模板 ===");
            System.out.println("目标streamId: " + newStreamId);
            System.out.println("目标streamListId: " + streamListId);
            System.out.println("待复制模板数量: " + (templatesToCopy != null ? templatesToCopy.size() : 0));
            
            if (templatesToCopy == null || templatesToCopy.isEmpty()) {
                System.out.println("没有模板需要复制，返回成功");
                return true;
            }

            // 检查该streamId是否已经有模板，如果有则直接返回成功（避免重复创建）
            List<NewTemplate> existingTemplates = newTemplateMapper.getTemplatesByStreamId(newStreamId);
            if (!existingTemplates.isEmpty()) {
                System.out.println("streamId " + newStreamId + " 已有 " + existingTemplates.size() + " 个模板，跳过复制");
                return true;
            }
            
            System.out.println("准备复制的模板列表:");
            for (NewTemplate template : templatesToCopy) {
                System.out.println("- ID: " + template.getId() + ", Name: " + template.getName() + ", PID: " + template.getPid());
            }

            // 直接复制所有模板，不进行过滤，确保父子关系完整性
            // 创建ID映射关系，用于维护父子关系
            Map<String, String> oldToNewIdMap = new HashMap<>();
            
            // 第一轮：为所有模板创建新ID映射
            for (NewTemplate template : templatesToCopy) {
                String newId = UUID.randomUUID().toString();
                oldToNewIdMap.put(template.getId(), newId);
                System.out.println("创建ID映射: " + template.getId() + " -> " + newId + " (模板: " + template.getName() + ")");
            }

            // 第二轮：复制模板并更新父子关系
            for (NewTemplate template : templatesToCopy) {
                NewTemplate newTemplate = new NewTemplate();
                
                // 复制基本信息
                newTemplate.setId(oldToNewIdMap.get(template.getId()));
                newTemplate.setFileId(template.getFileId());
                newTemplate.setTemplateId(template.getTemplateId());
                newTemplate.setStreamListId(streamListId);
                newTemplate.setStreamId(newStreamId);
                newTemplate.setBranchId(template.getBranchId());
                newTemplate.setBlockId(template.getBlockId());
                newTemplate.setName(template.getName());
                newTemplate.setType(template.getType());
                newTemplate.setDescription(template.getDescription());
                newTemplate.setAuth(template.getAuth());
                newTemplate.setSource(template.getSource());
                newTemplate.setVersion(template.getVersion());
                newTemplate.setDetail(template.getDetail());
                newTemplate.setModifyUser(template.getModifyUser());
                newTemplate.setModifyTime(new Date());
                newTemplate.setStatus(template.getStatus());
                
                // 更新父子关系 - 关键逻辑！
                if (template.getPid() != null && oldToNewIdMap.containsKey(template.getPid())) {
                    // 如果父ID在映射中存在，使用新的父ID
                    String newPid = oldToNewIdMap.get(template.getPid());
                    newTemplate.setPid(newPid);
                    System.out.println("更新父子关系: " + template.getName() + " 的pid从 " + template.getPid() + " 改为 " + newPid);
                } else if ("0".equals(template.getPid())) {
                    // 如果是根节点，保持为0
                    newTemplate.setPid("0");
                    System.out.println("保持根节点: " + template.getName() + " pid=0");
                } else {
                    // 其他情况保持原有pid（可能是外部引用）
                    newTemplate.setPid(template.getPid());
                    System.out.println("保持原有pid: " + template.getName() + " pid=" + template.getPid());
                }

                // 保存新模板
                boolean result = newTemplateMapper.addTemplate(newTemplate);
                if (!result) {
                    throw new RuntimeException("复制模板失败: " + template.getId());
                } else {
                    System.out.println("成功复制模板: " + template.getName() + " -> 新ID: " + newTemplate.getId());
                }
            }

            System.out.println("=== 批量复制模板完成，共复制 " + templatesToCopy.size() + " 个模板 ===");
            return true;
        } catch (Exception e) {
            System.out.println("批量复制模板失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("批量复制模板失败: " + e.getMessage());
        }
    }

    @Override
    public List<NewTemplate> getTemplatesByStreamId(String streamId) {
        try {
            return newTemplateMapper.getTemplatesByStreamId(streamId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
