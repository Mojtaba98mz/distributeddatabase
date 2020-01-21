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
}, {
    "value": "spark",
    "Name": "spark"
}];
var sparkValue = [{
    "value": "count_spark",
    "Name": "count_spark"
},
    {
        "value": "search_spark",
        "Name": "search_spark"
    }];
var word = [{
    "value": "word",
    "Name": "error"
}, {
    "value": "word",
    "Name": "log"
}, {
    "value": "word",
    "Name": "hello"
}];
$('#fetchData').on('click', function (event) {
    $("#simple").dxTextBox({
        value: "John Smith"
    });
    let coreNumber = $("#Cores").dxSelectBox('instance').option('value').value;
    let algorithm = $("#algorithm").dxSelectBox('instance').option('value').value;
    debugger
    $.get(baseUrl + algorithm + "?core=" + coreNumber, function (data, status) {
        debugger;
        $('#parentChart').addClass('chartstyle');
        $('#parentpie').addClass('piestyle');
        showChart(data);
        showPie(data);

    });
});
$('#spark_button').on('click', function (event) {
    debugger
    var wordSearach = $("#spark").dxSelectBox('instance').option('value').value;
    let url;
    if (wordSearach == 'search_spark') {
        url = baseUrl + "sparkSearch"+ "?wordSearch=" + wordSearach;
    }
    else {
        url=baseUrl + "sparkCount";
    }

    $.get(url, function (data, status) {
        $('#parentChart').addClass('chartstyle');
        // $('#parentpie').addClass('piestyle');
        debugger
        showDataTable(data);

    });
});
var test = [{"number": "1", "wordName": "a"}, {"number": "1", "wordName": "b"}, {"number": "1", "wordName": "c"}];

let showDataTable = (dataSource) =>
{
    $("#chart").dxDataGrid({
        dataSource: dataSource,
        showBorders: true,
        paging: {
            pageSize: 10
        },
        pager: {
            showPageSizeSelector: true,
            allowedPageSizes: [5, 10, 20],
            showInfo: true
        },
        columns: ["wordName", "number"]
    });
}
let showChart = (dataSource) =>
{
    $("#chart").dxChart({
        dataSource: dataSource,
        argumentField: "coreNumber",
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
$(function () {
    var Cores = $("#spark").dxSelectBox({
        dataSource: sparkValue,
        displayExpr: "Name",
        searchEnabled: true
    }).dxSelectBox("instance");
});
$(function () {
    var Cores = $("#word").dxSelectBox({
        dataSource: word,
        displayExpr: "Name",
        searchEnabled: true
    }).dxSelectBox("instance");
});


let showPie = (dataSource) =>
{

    var all = 0;
    var x;
    dataSource.forEach(function (arrayItem) {
        all = all + arrayItem.executionTime;
    });

    dataSource.forEach(function (arrayItem) {
        arrayItem.percent = (arrayItem.executionTime / all);

    });

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
                    customizeText: function (arg) {
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