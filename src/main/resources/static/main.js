var baseUrl = "http://localhost:8080/";
var coreNumber = [{
    "value": 2,
    "Name": "2"
}, {
    "value": 4,
    "Name": "4"
}, {
    "value": 8,
    "Name": "8"
}, {
    "value": 16,
    "Name": "16"
}, {
    "value": 32,
    "Name": "32"
}];
var algorithm = [{
    "value": "mergeAllSort",
    "Name": "mergeAllSort"
}, {
    "value": "redistributionMergeAllSort",
    "Name": "redistributionMergeAllSort"
}, {
    "value": "parallelPartitionedSort",
    "Name": "parallelPartitionedSort"
}, {
    "value": "binaryMergeSort",
    "Name": "binaryMergeSort"
}, {
    "value": "redistributionBinaryMergeSort",
    "Name": "redistributionBinaryMergeSort"
}];
$('#fetchData').on('click', function (event) {
    
    let coreNumber = $("#Cores").dxSelectBox('instance').option('value').value;
    let algorithm = $("#algorithm").dxSelectBox('instance').option('value').value;
    $.get(baseUrl + algorithm + "?core=" + coreNumber, function (data, status) {
        showChart(data);
        showPie(data);
    });
});


let showChart = (dataSource) => {
    $("#chart").dxChart({
        dataSource: dataSource,
        series: {
            argumentField: "coreNumber",
            hoverMode: "allArgumentPoints",
            valueField: "executionTime",
            name: "Cpu Cores",
            type: "bar",
            color: '#ff0000',
            label: {
                visible: true,
                format: {
                    type: "fixedPoint",
                    precision: 0,
                    color: '#ff0000'
                }
            }
        },
        legend: {
            verticalAlignment: "bottom",
            horizontalAlignment: "center"
        },
        "export": {
            enabled: true
        },
        valueAxis: {
            title: {
                text: "milliseconds"
            },
            position: "left"
        }

    });
}
;
$(function () {
    var Cores = $("#Cores").dxSelectBox({
        dataSource: coreNumber,
        displayExpr: "Name",
        searchEnabled: true
    }).dxSelectBox("instance");
});
$(function () {
    var Cores = $("#algorithm").dxSelectBox({
        dataSource: algorithm,
        displayExpr: "Name",
        searchEnabled: true
    }).dxSelectBox("instance");
});



let showPie = (dataSource) => {
    debugger
    var all=0;
    var x;
    dataSource.forEach(function (arrayItem) {
        all = all+arrayItem.executionTime;
    });

    dataSource.forEach(function (arrayItem) {
        arrayItem.percent= (arrayItem.executionTime/all);

    });
    debugger
    $("#pie").dxPieChart({
        palette: "bright",
        dataSource: dataSource,
        series: [
            {
                argumentField: "coreNumber",
                valueField: "percent",
                label: {
                    visible: true,
                    format: "percent",
                    connector: {
                        visible: true,
                        horizontalAlignment: "center",
                        verticalAlignment: "bottom",
                        width: 1
                    },
                    customizeText: function(arg) {
                        return arg.valueText + " ( cpu =" + arg.argument + ")";
                    }
                }
            }
        ],
        title: "cpu work",
        "export": {
            enabled: true
        }
    });
}