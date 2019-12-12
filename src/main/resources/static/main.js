var baseUrl = "http://localhost:8080/";
$('#fetchData').on('click', function (event) {
    let coreNumber = $('#number').val();
    let algorithm = $("input[name='algorithm']:checked").val();
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
};


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