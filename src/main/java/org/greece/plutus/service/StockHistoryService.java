package org.greece.plutus.service;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.StringUtils;
import org.greece.plutus.common.Constant;
import org.greece.plutus.model.DayStockTrade;
import org.greece.plutus.util.FileUtil;
import org.greece.plutus.util.HttpClientUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by hujiahua on 2018/11/10.
 */
public class StockHistoryService {

    public static final String HISTORY_DATA = Constant.WORK_HOME + "/history";

    private static final Pattern STOCK_INFO =  Pattern.compile("<td><div align=\"center\">[\\s\\S]+?(\\d{4}-\\d{2}-\\d{2})[\\s\\S]+?</div></td>"
            +"\\s+?<td><div align=\"center\">(\\S+)</div></td>"
            +"\\s+?<td><div align=\"center\">(\\S+)</div></td>"
            +"\\s+?<td><div align=\"center\">(\\S+)</div></td>"
            +"\\s+?<td class=\"tdr\"><div align=\"center\">(\\S+)</div></td>"
            +"\\s+?<td class=\"tdr\"><div align=\"center\">(\\S+)</div></td>"
            +"\\s+?<td class=\"tdr\"><div align=\"center\">(\\S+)</div></td>");

    public List<DayStockTrade> fetchStockHistory(String stockCode, String fromDate, String toDate){
        if(!FileUtil.containDirectory(HISTORY_DATA+"/"+stockCode)){

        }
        return null;
    }

    public void loadMarketHistoryToDisk(String stockCode) throws Exception {

        String dir = HISTORY_DATA+"/"+stockCode;
        FileUtil.removeDirIfExists(dir);
        FileUtil.mkdirIfNotExist(dir);
        List<DayStockTrade> list = new ArrayList<>(365);
        for (int year = 2018; year > 2000; year--) {
            for (int quarterNum = 4; quarterNum > 0; quarterNum--) {
                list.addAll(queryMarketHistory(stockCode,year,quarterNum));

            }
            if(list.size() > 0){
                FileUtil.writeStringToFile(new File(dir+"/"+year), String.join("\n",(List<String>)list.stream().map(s -> s.toCvsLine()).collect(Collectors.toList())));
                list.clear();
            }else {
                break;
            }


        }


    }

    public List<DayStockTrade> queryMarketHistory(String stockCode,Integer year, Integer quarterNum) throws Exception {

        List<DayStockTrade> list = new ArrayList<>();
        String url = String.format("http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/%s.phtml?year=%d&jidu=%d",stockCode,year,quarterNum);
        String resp = new String(HttpClientUtil.get(url,20000));
        Thread.sleep(5000);
        Matcher m = STOCK_INFO.matcher(resp);
        while(m.find()) {
            Integer groupCount =  m.groupCount();
            if(groupCount != 7){
                continue;
            }

            DayStockTrade dayStockTrade = new DayStockTrade();
            dayStockTrade.setTradeDate(m.group(1));
            dayStockTrade.setOpeningPrice(Float.valueOf(m.group(2)));
            dayStockTrade.setMaxPrice(Float.valueOf(m.group(3)));
            dayStockTrade.setClosingPrice(Float.valueOf(m.group(4)));
            dayStockTrade.setMinPrice(Float.valueOf(m.group(5)));
            dayStockTrade.setTradeShare(Long.valueOf(m.group(6)));
            dayStockTrade.setTradeAmount(Long.valueOf(m.group(7)));

            list.add(dayStockTrade);
        }
        return list;
    }


    public static void main(String[] args) throws Exception {
        StockHistoryService service = new StockHistoryService();
//        String code = "510050";
//        System.out.println(JSON.toJSONString(service.queryMarketHistory("600000",2017,1)));
//        service.loadMarketHistoryToDisk(code);

//        code = "5100500";
        String[] codes = new String[]{"510050","5100300","510500"};
        for (String code:codes) {
            service.loadMarketHistoryToDisk(code);
        }



//        String resp ="\t  <thead>\n" +
//                "\t\t\t<tr>\n" +
//                "\t\t\t\t<th colspan=\"7\">\n" +
//                "\t\t\t\t�ַ�����(600000)<FONT COLOR=\"blue\"></FONT>��<FONT COLOR=\"blue\"></FONT>������ʷ����\n" +
//                "\t\t\t\t</th>\n" +
//                "\t\t\t</tr>\n" +
//                "      </thead>\n" +
//                "      <tr class=\"tr_2\">\n" +
//                "        <td><div align=\"center\"><strong>����</strong></div></td>\n" +
//                "        <td><div align=\"center\"><strong>���̼�</strong></div></td>\n" +
//                "        <td><div align=\"center\"><strong>��\u07FC�</strong></div></td>\n" +
//                "        <td><div align=\"center\"><strong>���̼�</strong></div></td>\n" +
//                "        <td class=\"tdr\"><div align=\"center\"><strong>��ͼ�</strong></div></td>\n" +
//                "\t\t<td class=\"tdr\"><div align=\"center\"><strong>������(��)</strong></div></td>\n" +
//                "\t\t<td class=\"tdr\"><div align=\"center\"><strong>���\u05FD��(\u052A)</strong></div></td>\n" +
//                "      </tr>\n" +
//                "\t\t\t  <tr >\n" +
//                "\t\t\t<td><div align=\"center\">\n" +
//                "\t\t\t1999-12-30\t\t\t</div></td>\n" +
//                "\t\t\t<td><div align=\"center\">24.900</div></td>\n" +
//                "\t\t\t<td><div align=\"center\">24.990</div></td>\n" +
//                "\t\t\t<td><div align=\"center\">24.750</div></td>\n" +
//                "\t\t\t<td class=\"tdr\"><div align=\"center\">24.650</div></td>\n" +
//                "\t\t\t<td class=\"tdr\"><div align=\"center\">2333200</div></td>\n" +
//                "\t\t\t<td class=\"tdr\"><div align=\"center\">57888237</div></td>\n" +
//                "\t\t  </tr>\n" +
//                "\t\t\t\t  <tr class=\"tr_2\">\n" +
//                "\t\t\t<td><div align=\"center\">\n" +
//                "\t\t\t1999-12-29\t\t\t</div></td>\n" +
//                "\t\t\t<td><div align=\"center\">24.710</div></td>\n" +
//                "\t\t\t<td><div align=\"center\">24.970</div></td>\n" +
//                "\t\t\t<td><div align=\"center\">24.660</div></td>\n" +
//                "\t\t\t<td class=\"tdr\"><div align=\"center\">24.570</div></td>\n" +
//                "\t\t\t<td class=\"tdr\"><div align=\"center\">2284200</div></td>\n" +
//                "\t\t\t<td class=\"tdr\"><div align=\"center\">56436027</div></td>\n" +
//                "\t\t  </tr>";
//
//        Matcher m = STOCK_INFO.matcher(resp);
//        while(m.find()) {
////            System.out.println(m.group());
//            for (int i = 1; i <= m.groupCount(); i++) {
//                System.out.println(m.group(i));
//            }
//            System.out.println("-------\n");
//        }

    }

}
