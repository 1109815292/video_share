window.onload = function() {
	$(window.parent.document).find("#sidebar").removeClass("menu-compact");
	
		$("#contInfoList").css("min-height",window.screen.availHeight - 210 + "px");

		if ($(".sheetTable").length >0){
			$(".sheetTable tr:odd").addClass("oddBackground");
		}

		//凭证打印时，补空行
		var oDiv = document.getElementById("voucherFlag");
		if (oDiv != null){
			var oTable = document.getElementById("table-bills");
			var oTr = oTable.getElementsByTagName("tr");
			for ( var i = oTr.length; i < 8; i++) {
				CreateSpaceTB(i);
			}						
		}

		
		var oInput = document.getElementById("purState");
		if (oInput != null){
			var oImg = document.getElementById("imgState");
			if (oInput.value == "1") {
				$("#btnAuditing").hide();
				oImg.src = "img/stateSh.gif";
			}
			if (oInput.value == "2") {
				$("#btnAuditing").hide();
				oImg.src = "img/stateRk.gif";
			}	
			if (oInput.value == "3" || oInput.value == "5") {
				$("#btnPutOut").hide();
				$("#btnAuditing").hide();
			}
		}
		var oInput = document.getElementById("salesState");
		if (oInput != null){
			var oImg = document.getElementById("imgState");
			if (oInput.value == "1" || oInput.value == "2") {
				$("#btnAuditing").hide();
				oImg.src = "img/stateSh.gif";
			}
			if (oInput.value == "3") {
				$("#btnAuditing").hide();
				oImg.src = "img/stateCk.gif";
			}			
		}
		var oInput = document.getElementById("salesReturnState");
		if (oInput != null){
			var oImg = document.getElementById("imgState");
			if (oInput.value == "1") {
				$("#btnAuditing").hide();
				oImg.src = "img/stateSh.gif";
			}else if (oInput.value == "2"){
				$("#btnInbound").hide();
				oImg.src = "img/C_131.png";
			}else if (oInput.value >= "3") {
				$("#btnAuditing").hide();
				$("#btnInbound").hide();
				oImg.src = "img/C_131.png";
			}			
		}
		
		var dialog = $("div.testDialog").dialog({
			position : {
				my : "left top",
				at : "left bottom+50",
				of : ".SettingsBox"
			},
			width : "600",
			title : "Print Dialog Box Contents"
		});

		$(".toggleDialog").click(function() {
			dialog.dialog("open");
		});

		$("div.b1")
				.click(
						function() {

							var mode = $("input[name='mode']:checked").val();
							var close = mode == "popup"
									&& $("input#closePop").is(":checked");
							var extraCss = $("input[name='extraCss']").val();

							var print = "";
							$("input.selPA:checked").each(
									function() {
										print += (print.length > 0 ? "," : "")
												+ "div.PrintArea."
												+ $(this).val();
									});

							var keepAttr = [];
							$(".chkAttr").each(function() {
								if ($(this).is(":checked") == false)
									return;

								keepAttr.push($(this).val());
							});

							var headElements = $("input#addElements").is(
									":checked") ? '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>'
									: '';

							var options = {
								mode : mode,
								popClose : close,
								extraCss : extraCss,
								retainAttr : keepAttr,
								extraHead : headElements
							};

							$(print).printArea(options);
						});

		$("input[name='mode']").click(function() {
			if ($(this).val() == "iframe")
				$("#closePop").attr("checked", false);
		});

	}
	function billPrint() {
		$(".auditingState").hide();
		var mode = $("input[name='mode']:checked").val();
		var close = mode == "popup" && $("input#closePop").is(":checked");
		var extraCss = $("input[name='extraCss']").val();

		var print = "";
		$("input.selPA:checked").each(
				function() {
					print += (print.length > 0 ? "," : "") + "div.PrintArea."
							+ $(this).val();
				});

		var keepAttr = [];
		$(".chkAttr").each(function() {
			if ($(this).is(":checked") == false)
				return;

			keepAttr.push($(this).val());
		});

		var headElements = $("input#addElements").is(":checked") ? '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>'
				: '';

		var options = {
			mode : mode,
			popClose : close,
			extraCss : extraCss,
			retainAttr : keepAttr,
			extraHead : headElements
		};

		$(print).printArea(options);
		$(".auditingState").show();
	}
	function CreateSpaceTB(i) {
		var oTB = document.getElementById("table-bills").getElementsByTagName(
				"tbody")[0];
		var oTR = oTB.insertRow(oTB.rows.length);

		oTR.style.height = "50px";
		var oTD0 = oTR.insertCell(0);
		oTD0.innerHTML = "";
		var oTD1 = oTR.insertCell(1);
		oTD1.innerHTML = "";
		var oTD2 = oTR.insertCell(2);
		oTD2.innerHTML = "";
		var oTD3 = oTR.insertCell(3);
		oTD3.innerHTML = "";
	}
	function auditing() {
		$("#imgState").hide();
		$("#imgState").attr("src", "img/stateJz.gif");
		$("#imgState").fadeIn();
	}
	
	//记账提示框
	//如果页面有该提示框，且有返回值，则显示提示框，同时启动定时器2秒后关闭该提示框
	var alertDiv=document.getElementById("resultInput");
	if (alertDiv != null){
		if (alertDiv.innerHTML == "ok"){
			$("#alertSuccess").show("slow");
			setTimeout("closeAlert()",2000);
		}else if (alertDiv.innerHTML == "error"){
			$("#alertfalier").show("slow");
			setTimeout("closeAlert()",5000);			
		}
		$(alertDiv).html("");
	}
	
	//关闭提示框
	function closeAlert(){
		$("#alertSuccess").hide("slow");
		$("#alertfalier").hide("slow");
	}
	
	function openSaveModal(){
		$("#saveModal").modal("show");
	}
	
	
	function preVoucherBill(){
		var noStr = $("#billNo").html();
		var billNoStr = noStr.substring(11,15);
		var billNo = parseInt(billNoStr);
		if (billNo > 1){
			var preNoStr = (billNo - 1).toString();
			for (var i=preNoStr.length;i<4;i++){
				preNoStr = "0" + preNoStr;
			}
			window.location.href = "voucherBills-auditing.action?billNo=" + noStr.substring(0,11) + preNoStr;
		} 
	}
	
	function nextVoucherBill(){		
		var noStr = $("#billNo").html();
		var maxNo = $("#maxNo").html();
		var billNoStr = noStr.substring(11,15);
		var billNo = parseInt(billNoStr);
		if (billNo < maxNo){
			var nextNoStr = (billNo + 1).toString();
			for (var i=nextNoStr.length;i<4;i++){
				nextNoStr = "0" + nextNoStr;
			}
			window.location.href = "voucherBills-auditing.action?billNo=" + noStr.substring(0,11) + nextNoStr;			
		}
	}
