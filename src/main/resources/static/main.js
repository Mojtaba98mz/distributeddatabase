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
    debugger;
    let coreNumber = $("#Cores").dxSelectBox('instance').option('value').value;
    let algorithm = $("#algorithm").dxSelectBox('instance').option('value').value;
    $.get(baseUrl + algorithm + "?core=" + coreNumber, function (data, status) {
        showChart(data);
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
            color: '#ff7e57',
            label: {
                visible: true,
                format: {
                    type: "fixedPoint",
                    precision: 0,
                    color: '#ff0300'
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
        onPointClick: function (e) {
            debugger;
            e.target.select();
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