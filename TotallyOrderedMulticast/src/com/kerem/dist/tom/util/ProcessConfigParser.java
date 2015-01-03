package com.kerem.dist.tom.util;

import com.google.gson.stream.JsonReader;
import com.kerem.dist.tom.model.ProcessConfigModel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class to parse a process configuration from the
 * file with it's name passed as argument to the readConfigFile(String configFileName) method.
 *
 * Created by keremgocen on 12/30/14.
 */
public class ProcessConfigParser {

    private static final String P_ID = "id";
    private static final String P_COUNT = "total_processes";
    private static final String P_PORT = "port";

    /**
     * Returns a ProcessConfigModel object which has the configuration
     * parameters for a specific process, where process configuration file
     * name is passed as argument.
     * <p>
     * This method uses JsonReader class from gson library
     *
     * @param   configFileName      path to the configuration file to be parsed
     * @return  ProcessConfigModel  with the config information
     */
    public ProcessConfigModel readConfigFile(String configFileName) {

        ProcessConfigModel config = new ProcessConfigModel();
        List<Integer> portList = new ArrayList<Integer>();

        try {
            JsonReader reader = new JsonReader(new FileReader(configFileName));

            reader.beginObject();

            while (reader.hasNext()) {

                String name = reader.nextName();

                if (name.equals("processId")) {

                    int pid = reader.nextInt();
                    config.setProcessId(pid);

                } else if (name.equals("totalProcesses")) {

                    int totalProcess = reader.nextInt();
                    config.setTotalNumberOfProcesses(totalProcess);

                } else if (name.equals("ports")) {

                    // read array
                    reader.beginArray();

                    while (reader.hasNext()) {
                        int portNumber = reader.nextInt();
                        portList.add(portNumber);
                    }

                    config.setPortList(portList);

                    reader.endArray();

                } else {
                    reader.skipValue(); //avoid some unhandled events
                }
            }

            reader.endObject();
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return config;
    }

}
