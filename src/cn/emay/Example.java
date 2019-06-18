package cn.emay;

import java.util.HashMap;
import java.util.Map;

import cn.emay.eucp.inter.framework.dto.CustomSmsIdAndMobile;
import cn.emay.eucp.inter.framework.dto.CustomSmsIdAndMobileAndContent;
import cn.emay.eucp.inter.framework.dto.PersonalityParams;
import cn.emay.eucp.inter.http.v1.dto.request.BalanceRequest;
import cn.emay.eucp.inter.http.v1.dto.request.MoRequest;
import cn.emay.eucp.inter.http.v1.dto.request.ReportRequest;
import cn.emay.eucp.inter.http.v1.dto.request.SmsBatchOnlyRequest;
import cn.emay.eucp.inter.http.v1.dto.request.SmsBatchRequest;
import cn.emay.eucp.inter.http.v1.dto.request.SmsPersonalityAllRequest;
import cn.emay.eucp.inter.http.v1.dto.request.SmsPersonalityRequest;
import cn.emay.eucp.inter.http.v1.dto.request.SmsSingleRequest;
import cn.emay.eucp.inter.http.v1.dto.response.BalanceResponse;
import cn.emay.eucp.inter.http.v1.dto.response.MoResponse;
import cn.emay.eucp.inter.http.v1.dto.response.ReportResponse;
import cn.emay.eucp.inter.http.v1.dto.response.SmsResponse;
import cn.emay.util.AES;
import cn.emay.util.GZIPUtils;
import cn.emay.util.JsonHelper;
import cn.emay.util.http.HttpClient;
import cn.emay.util.http.HttpRequest;
import cn.emay.util.http.HttpRequestBytes;
import cn.emay.util.http.HttpRequestParams;
import cn.emay.util.http.HttpResponseBytes;
import cn.emay.util.http.HttpResponseBytesPraser;
import cn.emay.util.http.HttpResultCode;
import cn.emay.util.http.HttpsRequestBytes;

public class Example {

	public static void main(String[] args) {
		// appId
		String appId = "EUCP-EMY-SMS1-11111";// 请联系销售，或者在页面中 获取
		// 密钥
		String secretKey = "1111111111111111";// 请联系销售，或者在页面中 获取
		// 接口地址
		String host = "http://emay.com";// 请联系销售获取
		// 加密算法
		String algorithm = "AES/ECB/PKCS5Padding";
		// 编码
		String encode = "UTF-8";
		// 是否压缩
		boolean isGizp = true;

		// 获取余额
		getBalance(appId, secretKey, host, algorithm, isGizp, encode);
		// 获取状态报告
		getReport(appId, secretKey, host, algorithm, isGizp, encode);
		// 获取上行
		getMo(appId, secretKey, host, algorithm, isGizp, encode);
		// 发送单条短信
		setSingleSms(appId, secretKey, host, algorithm, "【某某公司】您的验证码是123", null, null, "12100000000", isGizp, encode);//短信内容请以商务约定的为准，如果已经在通道端绑定了签名，则无需在这里添加签名
		// // 发送批次短信[有自定义SMSID]
		setBatchSms(appId, secretKey, host, algorithm, "【某某公司】您的验证码是123", null,
				new CustomSmsIdAndMobile[] { new CustomSmsIdAndMobile("1", "12100000000"), new CustomSmsIdAndMobile("2", "12100000000") }, isGizp, encode);
		// // 发送批次短信[无自定义SMSID]
		setBatchOnlySms(appId, secretKey, host, algorithm, "【某某公司】您的验证码是123", null, new String[] { "12100000000", "12100000001" }, isGizp, encode);
		// // 发送个性短信
		setPersonalitySms(appId, secretKey, host, algorithm, null, new CustomSmsIdAndMobileAndContent[] { new CustomSmsIdAndMobileAndContent("1", "12100000000", "【某某公司】您的验证码是123"),
				new CustomSmsIdAndMobileAndContent("2", "12100000001", "【某某公司】您的验证码是123") }, isGizp, encode);
		// // 发送全个性短信
		setPersonalityAllSms(appId, secretKey, host, algorithm, new PersonalityParams[] { new PersonalityParams("101", "12100000000", "【天气不错0", "01", null),
				new PersonalityParams("102", "12100000001", "天气不错1", "02", null) }, isGizp, encode);
	}

	/**
	 * 获取余额
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void getBalance(String appId, String secretKey, String host, String algorithm, boolean isGzip, String encode) {
		System.out.println("=============begin getBalance==================");
		BalanceRequest pamars = new BalanceRequest();
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/getBalance", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			BalanceResponse response = JsonHelper.fromJson(BalanceResponse.class, result.getResult());
			if (response != null) {
				System.out.println("result data : " + response.getBalance());
			}
		}
		System.out.println("=============end getBalance==================");
	}

	/**
	 * 获取状态报告
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void getReport(String appId, String secretKey, String host, String algorithm, boolean isGzip, String encode) {
		System.out.println("=============begin getReport==================");
		ReportRequest pamars = new ReportRequest();
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/getReport", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			ReportResponse[] response = JsonHelper.fromJson(ReportResponse[].class, result.getResult());
			if (response != null) {
				for (ReportResponse d : response) {
					System.out.println("result data : " + d.getExtendedCode() + "," + d.getMobile() + "," + d.getCustomSmsId() + "," + d.getSmsId() + "," + d.getState() + "," + d.getDesc() + ","
							+ d.getSubmitTime() + "," + d.getReceiveTime());
				}
			}
		}
		System.out.println("=============end getReport==================");
	}

	/**
	 * 获取上行
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void getMo(String appId, String secretKey, String host, String algorithm, boolean isGzip, String encode) {
		System.out.println("=============begin getMo==================");
		MoRequest pamars = new MoRequest();
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/getMo", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			MoResponse[] response = JsonHelper.fromJson(MoResponse[].class, result.getResult());
			if (response != null) {
				for (MoResponse d : response) {
					System.out.println("result data:" + d.getContent() + "," + d.getExtendedCode() + "," + d.getMobile() + "," + d.getMoTime());
				}
			}
		}
		System.out.println("=============end getMo==================");
	}

	/**
	 * 发送单条短信
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void setSingleSms(String appId, String secretKey, String host, String algorithm, String content, String customSmsId, String extendCode, String mobile, boolean isGzip, String encode) {
		System.out.println("=============begin setSingleSms==================");
		SmsSingleRequest pamars = new SmsSingleRequest();
		pamars.setContent(content);
		pamars.setCustomSmsId(customSmsId);
		pamars.setExtendedCode(extendCode);
		pamars.setMobile(mobile);
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/sendSingleSMS", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			SmsResponse response = JsonHelper.fromJson(SmsResponse.class, result.getResult());
			if (response != null) {
				System.out.println("data : " + response.getMobile() + "," + response.getSmsId() + "," + response.getCustomSmsId());
			}
		}
		System.out.println("=============end setSingleSms==================");
	}

	/**
	 * 发送批次短信
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void setBatchOnlySms(String appId, String secretKey, String host, String algorithm, String content, String extendCode, String[] mobiles, boolean isGzip, String encode) {
		System.out.println("=============begin setBatchOnlySms==================");
		SmsBatchOnlyRequest pamars = new SmsBatchOnlyRequest();
		pamars.setMobiles(mobiles);
		pamars.setExtendedCode(extendCode);
		pamars.setContent(content);
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/sendBatchOnlySMS", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setBatchOnlySms==================");
	}

	/**
	 * 发送批次短信
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void setBatchSms(String appId, String secretKey, String host, String algorithm, String content, String extendCode, CustomSmsIdAndMobile[] customSmsIdAndMobiles, boolean isGzip,
			String encode) {
		System.out.println("=============begin setBatchSms==================");
		SmsBatchRequest pamars = new SmsBatchRequest();
		pamars.setSmses(customSmsIdAndMobiles);
		pamars.setExtendedCode(extendCode);
		pamars.setContent(content);
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/sendBatchSMS", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setBatchSms==================");
	}

	/**
	 * 发送个性短信
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void setPersonalitySms(String appId, String secretKey, String host, String algorithm, String extendCode, CustomSmsIdAndMobileAndContent[] customSmsIdAndMobileAndContents,
			boolean isGzip, String encode) {
		System.out.println("=============begin setPersonalitySms==================");
		SmsPersonalityRequest pamars = new SmsPersonalityRequest();
		pamars.setSmses(customSmsIdAndMobileAndContents);
		pamars.setExtendedCode(extendCode);
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/sendPersonalitySMS", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setPersonalitySms==================");
	}

	/**
	 * 发送个性短信
	 * 
	 * @param isGzip
	 *            是否压缩
	 */
	private static void setPersonalityAllSms(String appId, String secretKey, String host, String algorithm, PersonalityParams[] customSmsIdAndMobileAndContents, boolean isGzip, String encode) {
		System.out.println("=============begin setPersonalityAllSms==================");
		SmsPersonalityAllRequest pamars = new SmsPersonalityAllRequest();
		pamars.setSmses(customSmsIdAndMobileAndContents);
		ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/sendPersonalityAllSMS", isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setPersonalityAllSms==================");
	}

	/**
	 * 公共请求方法
	 */
	public static ResultModel request(String appId, String secretKey, String algorithm, Object content, String url, final boolean isGzip, String encode) {
		Map<String, String> headers = new HashMap<String, String>();
		HttpRequest<byte[]> request = null;
		try {
			headers.put("appId", appId);
			headers.put("encode", encode);
			String requestJson = JsonHelper.toJsonString(content);
			System.out.println("result json: " + requestJson);
			byte[] bytes = requestJson.getBytes(encode);
			System.out.println("request data size : " + bytes.length);
			if (isGzip) {
				headers.put("gzip", "on");
				bytes = GZIPUtils.compress(bytes);
				System.out.println("request data size [com]: " + bytes.length);
			}
			byte[] parambytes = AES.encrypt(bytes, secretKey.getBytes(), algorithm);
			System.out.println("request data size [en] : " + parambytes.length);
			HttpRequestParams<byte[]> params = new HttpRequestParams<byte[]>();
			params.setCharSet("UTF-8");
			params.setMethod("POST");
			params.setHeaders(headers);
			params.setParams(parambytes);
			params.setUrl(url);
			if (url.startsWith("https://")) {
				request = new HttpsRequestBytes(params, null);
			} else {
				request = new HttpRequestBytes(params);
			}
		} catch (Exception e) {
			System.out.println("加密异常");
			e.printStackTrace();
		}
		HttpClient client = new HttpClient();
		String code = null;
		String result = null;
		try {
			HttpResponseBytes res = client.service(request, new HttpResponseBytesPraser());
			if (res == null) {
				System.out.println("请求接口异常");
				return new ResultModel(code, result);
			}
			if (res.getResultCode().equals(HttpResultCode.SUCCESS)) {
				if (res.getHttpCode() == 200) {
					code = res.getHeaders().get("result");
					if (code.equals("SUCCESS")) {
						byte[] data = res.getResult();
						System.out.println("response data size [en and com] : " + data.length);
						data = AES.decrypt(data, secretKey.getBytes(), algorithm);
						if (isGzip) {
							data = GZIPUtils.decompress(data);
						}
						System.out.println("response data size : " + data.length);
						result = new String(data, encode);
						System.out.println("response json: " + result);
					}
				} else {
					System.out.println("请求接口异常,请求码:" + res.getHttpCode());
				}
			} else {
				System.out.println("请求接口网络异常:" + res.getResultCode().getCode());
			}
		} catch (Exception e) {
			System.out.println("解析失败");
			e.printStackTrace();
		}
		ResultModel re = new ResultModel(code, result);
		return re;
	}

}
