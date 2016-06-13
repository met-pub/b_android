package com.techjumper.commonres.util;

import com.techjumper.plugincommunicateengine.Constants;
import com.techjumper.plugincommunicateengine.HostDataBuilder;
import com.techjumper.plugincommunicateengine.PluginEngine;

/**
 * Created by kevin on 16/6/13.
 */
public class PluginEngineUtil {

    public static final String PLUGIN_PROPERTY = "com.techjumper.polyhome.b.property";
    public static final String PLUGIN_INFO = "com.techjumper.polyhome.b.info";
    public static final String PLUGIN_SMARTHOME = "com.polyhome.sceneanddevice";
    public static final String PLUGIN_SETTING = "com.techjumper.polyhome.b.setting";
    public static final String PLUGIN_MEDICAL = "pltk.com.medical";
    public static final String PLUGIN_TALK = "com.dnake.talk";

    //物业
    public static void startProperty() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_PROPERTY)
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //信息
    public static void startInfo() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_INFO)
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //智能家居
    public static void startSmartHome() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_SMARTHOME)
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //设置
    public static void startSetting() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_SETTING)
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //医疗检测
    public static void startMedical() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_MEDICAL)
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }

    //对讲
    public static void startTalk() {
        PluginEngine.getInstance().start(new PluginEngine.IPluginConnection() {
            @Override
            public void onEngineConnected(PluginEngine.PluginExecutor pluginExecutor) {
                String data
                        = HostDataBuilder.startPluginBuilder()
                        .packageName(PLUGIN_TALK)
                        .build();
                try {
                    pluginExecutor.send(Constants.CODE_START_PLUGIN, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEngineDisconnected() {

            }
        });
    }
}