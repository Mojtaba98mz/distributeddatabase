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
            valueField: "executionTime",
            name: "Cpu Cores",
            type: "bar",
            color: '#ffaa66'
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

/*$("#chart").dxChart({
    palette: "soft",
    dataSource: dataSource,
    commonSeriesSettings: {
        barPadding: 0.5,
        argumentField: "state",
        type: "bar"
    },
    series: [
        {valueField: "year1970", name: "1970"},
        {valueField: "year1980", name: "1980"},
        {valueField: "year1990", name: "1990"},
        {valueField: "year2000", name: "2000"},
        {valueField: "year2008", name: "2008"},
        {valueField: "year2009", name: "2009"}
    ],
    legend: {
        verticalAlignment: "bottom",
        horizontalAlignment: "center"
    },
    "export": {
        enabled: true
    },
    title: {
        text: "Oil Production",
        subtitle: {
            text: "(in millions tonnes)"
        }
    }
});*/