package com.hanl.data.transform.model;

import java.io.Serializable;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/3
 * @desc: 数据转换命令管道, 是由多种配置转换规则组合而成的一个数据转换管道
 */
public class CommandPipeline implements Serializable {

    private static final long serialVersionUID = -2398090491675922927L;
    /**
     * 唯一ID
     */
    private String id;

    /**
     * 一个规则的过程的描述
     */
    private String processDesc;

    /**
     * 具体的规则组合
     */
    private List commands = new ArrayList();

    private Map<String, Object> commandsMap = new HashMap<>();


    public CommandPipeline(String id) {

        this(id, Collections.emptyList());

    }

    public CommandPipeline(String id, List<String> importCommands) {
        this.id = id;
        commandsMap.put("id", id);
        List<String> importCommandsTmp = new ArrayList<>();
        importCommandsTmp.add("org.kitesdk.**");
        importCommandsTmp.addAll(importCommands);
        commandsMap.put("importCommands", importCommandsTmp);
        commandsMap.put("commands", commands);
    }

    public synchronized void addImports(List<String> imports) {
        List<String> importTmp = (List<String>) commandsMap.get("importCommands");
        importTmp.addAll(imports);
        commandsMap.put("importCommands", importTmp);
    }

    public CommandPipeline addCommand(Map<String, Object> command) {
        commands.add(command);
        return this;
    }

    public CommandPipeline addAllCommand(List<Map<String, Object>> commandList) {
        commands.add(commandList);
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessDesc() {
        return processDesc;
    }

    public void setProcessDesc(String processDesc) {
        this.processDesc = processDesc;
    }

    public Map<String, Object> get() {
        return commandsMap;
    }

    public void setCommandsMap(Map<String, Object> commandsMap) {
        this.commandsMap = commandsMap;
    }


    public static CommandPipeline build(String id) {

        return new CommandPipeline(id, Collections.emptyList());
    }

    public static CommandPipeline build(String id, List<String> importCommands) {

        return new CommandPipeline(id, importCommands);
    }

    @Override
    public String toString() {
        return "CommandPipeline{" +
                "id='" + id + '\'' +
                ", processDesc='" + processDesc + '\'' +
                ", commands=" + commands +
                ", commandsMap=" + commandsMap +
                '}';
    }
}
