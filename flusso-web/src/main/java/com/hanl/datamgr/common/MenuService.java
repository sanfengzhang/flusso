package com.hanl.datamgr.common;

import com.hanl.datamgr.entity.CommandEntity;
import com.hanl.datamgr.entity.CommandInstanceEntity;
import com.hanl.datamgr.repository.CommandRepository;
import com.hanl.datamgr.vo.MenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/10/25
 * @desc:
 */
@Service
public class MenuService {

    @Autowired
    private CommandRepository commandRepository;

    public List<MenuVO<String>> getMainMenuData() {
        List<CommandEntity> allCommands = commandRepository.findAll();
        //------先按type进行分组
        Map<String, List<CommandEntity>> typeForCmd = new HashMap<>();
        for (CommandEntity entity : allCommands) {
            String type = entity.getCommandType();
            List<CommandEntity> list = typeForCmd.get(type);
            if (null == list) {
                list = new ArrayList<>();
                typeForCmd.put(type, list);
            }
            list.add(entity);
        }
        List<MenuVO<String>> menu = new ArrayList<>();
        Iterator<Map.Entry<String, List<CommandEntity>>> it = typeForCmd.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<CommandEntity>> en = it.next();
            List<CommandEntity> commandEntityList = en.getValue();

            MenuVO<String> first = new MenuVO<>();//初始化一级菜单
            first.setData(en.getKey());
            first.setType(en.getKey());
            first.setName(en.getKey());
            first.setIco("el-icon-menu");
            List<MenuVO<CommandEntity>> second = new ArrayList<>();
            for (CommandEntity entity : commandEntityList) {
                MenuVO<CommandEntity> secondItem = new MenuVO<>();//一级菜单子菜单
                secondItem.setName(entity.getCommandName());
                secondItem.setType(entity.getCommandName());
                secondItem.setIco("el-icon-menu");
                //--------------------
                CommandEntity tmp=new CommandEntity();
                BeanUtils.copyProperties(entity,tmp);
                secondItem.setData(tmp);

                List<CommandInstanceEntity> instanceEntityList = entity.getCommandInstanceEntityList();
                List<MenuVO<CommandInstanceEntity>> third = new ArrayList<>();
                for (CommandInstanceEntity instanceEntity : instanceEntityList) {
                    MenuVO<CommandInstanceEntity> thirdItem = new MenuVO<>();//一级菜单子菜单
                    thirdItem.setName(instanceEntity.getCommandInstanceName());
                    thirdItem.setData(instanceEntity);
                    thirdItem.setIco("el-icon-goods");
                    thirdItem.setType(instanceEntity.getCommandInstanceName());
                    third.add(thirdItem);
                }
                secondItem.getChildren().addAll(third);
                second.add(secondItem);
            }
            first.getChildren().addAll(second);
            menu.add(first);
        }
        return menu;
    }
}
