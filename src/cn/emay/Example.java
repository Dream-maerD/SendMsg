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
		String appId = "EUCP-EMY-SMS1-6U9KH";// 请联系销售，或者在页面中 获取
		// 密钥
		String secretKey = "565191";// 请联系销售，或者在页面中 获取
		// 接口地址
		String host = "http://sdk4report.eucp.b2m.cn:8080/sdk/SDKService?wsdl";// 请联系销售获取
		// 加密算法
		String algorithm = "AES/ECB/PKCS5Padding";
		// 编码
		String encode = "UTF-8";
		// 是否压缩
		boolean isGizp = true;
		//手机号
		String mobile = "18037452531";

		// 发送单条短信
		setSingleSms(appId, secretKey, host, algorithm, "【中远海运租赁】新接口，短信发送测试。", null, null, mobile, isGizp, encode);//短信内容请以商务约定的为准，如果已经在通道端绑定了签名，则无需在这里添加签名
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
		ResultModel result = request(appId, secretKey, algorithm, pamars, host, isGzip, encode);
		System.out.println("result code :" + result.getCode());
		if ("SUCCESS".equals(result.getCode())) {
			SmsResponse response = JsonHelper.fromJson(SmsResponse.class, result.getResult());
			if (response != null) {
				System.out.println("data : " + response.getMobile() + "," + response.getSmsId() + "," + response.getCustomSmsId());
				System.out.println(response);
			}
		}
		System.out.println("=============end setSingleSms==================");
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
