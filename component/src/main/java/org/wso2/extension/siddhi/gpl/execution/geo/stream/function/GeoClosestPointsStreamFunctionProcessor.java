/*
 * Copyright (c)  2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.extension.siddhi.gpl.execution.geo.stream.function;

import com.vividsolutions.jts.geom.Coordinate;
import org.wso2.extension.siddhi.gpl.execution.geo.internal.util.ClosestOperation;
import org.wso2.extension.siddhi.gpl.execution.geo.internal.util.GeoOperation;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.ReturnAttribute;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.query.processor.stream.function.StreamFunctionProcessor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.AbstractDefinition;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Find the closest geo point
 **/
@Extension(
        name = "closestPoints",
        namespace = "geo",
        description = "This will return the closest geo point to the geo.json.geometry.fence",
        examples = @Example(description = "this will return 0.5, 0.5, 0.5, 0.5 ",
                syntax = "closestPoints(0.5,0.5,\"{'type':'Polygon'," +
                        "'coordinates':[[[0,0],[0,2],[1,2],[1,0],[0,0]]]}\")"),
        parameters = {
                @Parameter(
                        name = "longitude",
                        description = "longitude value of the location",
                        type = DataType.DOUBLE
                ),
                @Parameter(
                        name = "latitude",
                        description = "latitude value of the location",
                        type = DataType.DOUBLE
                ),
                @Parameter(
                        name = "geo.json.geometry.fence",
                        description = "string value of the json object which contains the details of the geo" +
                                " geometry fence.",
                        type = DataType.STRING

                )
        },
        returnAttributes = {
                @ReturnAttribute(name = "closestPointOf1From2Latitude", description = "closest point's latitude to " +
                        "the fence from the location", type = {DataType.DOUBLE}),
                @ReturnAttribute(name = "closestPointOf1From2Longitude", description = "closest point's longitude " +
                        "to the fence from the location", type = {DataType.DOUBLE}),
                @ReturnAttribute(name = "closestPointOf2From1Latitude", description = "closest point's latitude to " +
                        "the location from the fence", type = {DataType.DOUBLE}),
                @ReturnAttribute(name = "closestPointOf2From1Longitude", description = "closest point's longitude to" +
                        " the location from the fence", type = {DataType.DOUBLE})
        }
)
public class GeoClosestPointsStreamFunctionProcessor extends StreamFunctionProcessor {

    GeoOperation geoOperation;

    @Override
    protected List<Attribute> init(AbstractDefinition abstractDefinition, ExpressionExecutor[] expressionExecutors,
                                   ConfigReader configReader,
                                   SiddhiAppContext siddhiAppContext) {
        this.geoOperation = new ClosestOperation();
        this.geoOperation.init(attributeExpressionExecutors, 0, attributeExpressionExecutors.length);
        List<Attribute> attributeList = new ArrayList<Attribute>(4);
        attributeList.add(new Attribute("closestPointOf1From2Latitude", Attribute.Type.DOUBLE));
        attributeList.add(new Attribute("closestPointOf1From2Longitude", Attribute.Type.DOUBLE));
        attributeList.add(new Attribute("closestPointOf2From1Latitude", Attribute.Type.DOUBLE));
        attributeList.add(new Attribute("closestPointOf2From1Longitude", Attribute.Type.DOUBLE));
        return attributeList;
    }

    @Override
    protected Object[] process(Object[] data) {
        Coordinate[] coordinates = (Coordinate[]) geoOperation.process(data);

        return new Object[]{coordinates[0].x, coordinates[0].y, coordinates[1].x, coordinates[1].y};
    }

    @Override
    protected Object[] process(Object o) {
        return new Object[0];
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Map<String, Object> currentState() {
        return new HashMap<String, Object>();
    }

    @Override
    public void restoreState(Map<String, Object> objects) {

    }
}
