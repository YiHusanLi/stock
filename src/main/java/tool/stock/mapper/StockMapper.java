package tool.stock.mapper;

import org.springframework.stereotype.Component;
import tool.stock.dto.twse.BwibbuAllDto;
import tool.stock.dto.twse.CompanyBasicDto;
import tool.stock.dto.twse.RevenueMonthlyDto;
import tool.stock.dto.twse.StockDayAllDto;
import tool.stock.entity.RevenueMonthly;
import tool.stock.entity.StockBasic;
import tool.stock.entity.StockDaily;
import tool.stock.entity.StockValuation;
import tool.stock.util.DateUtils;
import tool.stock.util.NumberUtils;

@Component
public class StockMapper {

    public StockDaily toStockDaily(StockDayAllDto dto) {
        StockDaily entity = new StockDaily();
        entity.setTradeDate(DateUtils.parseTwDate(dto.getDate()));
        entity.setStockCode(dto.getCode());
        entity.setStockName(dto.getName());
        entity.setTradeVolume(NumberUtils.toLong(dto.getTradeVolume()));
        entity.setTradeValue(NumberUtils.toLong(dto.getTradeValue()));
        entity.setOpeningPrice(NumberUtils.toBigDecimal(dto.getOpeningPrice()));
        entity.setHighestPrice(NumberUtils.toBigDecimal(dto.getHighestPrice()));
        entity.setLowestPrice(NumberUtils.toBigDecimal(dto.getLowestPrice()));
        entity.setClosingPrice(NumberUtils.toBigDecimal(dto.getClosingPrice()));
        entity.setPriceChange(NumberUtils.toBigDecimal(dto.getChange()));
        entity.setTransactionCount(NumberUtils.toLong(dto.getTransaction()));
        return entity;
    }

    public StockValuation toStockValuation(BwibbuAllDto dto) {
        StockValuation entity = new StockValuation();
        entity.setTradeDate(DateUtils.parseTwDate(dto.getDate()));
        entity.setStockCode(dto.getCode());
        entity.setStockName(dto.getName());
        entity.setPeRatio(NumberUtils.toBigDecimal(dto.getPEratio()));
        entity.setDividendYield(NumberUtils.toBigDecimal(dto.getDividendYield()));
        entity.setPbRatio(NumberUtils.toBigDecimal(dto.getPBratio()));
        return entity;
    }

    public StockBasic toStockBasic(CompanyBasicDto dto) {
        StockBasic entity = new StockBasic();
        entity.setStockCode(dto.getStockCode());
        entity.setCompanyName(dto.getCompanyName());
        entity.setCompanyShortName(dto.getCompanyShortName());
        entity.setEnglishShortName(dto.getEnglishShortName());
        entity.setIndustryType(dto.getIndustryType());
        entity.setChairman(dto.getChairman());
        entity.setGeneralManager(dto.getGeneralManager());
        entity.setSpokesperson(dto.getSpokesperson());
        entity.setCapitalAmount(NumberUtils.toLong(dto.getCapitalAmount()));
        entity.setIssuedCommonShares(NumberUtils.toLong(dto.getIssuedCommonShares()));
        entity.setListingDate(DateUtils.parseTwDate(dto.getListingDate()));
        entity.setEstablishmentDate(DateUtils.parseTwDate(dto.getEstablishmentDate()));
        entity.setCompanyAddress(dto.getAddress());
        entity.setCompanyPhone(dto.getPhone());
        entity.setWebsite(dto.getWebsite());
        return entity;
    }

    public RevenueMonthly toRevenueMonthly(RevenueMonthlyDto dto) {
        RevenueMonthly entity = new RevenueMonthly();

        String ym = dto.getDataYearMonth();
        if (ym != null && ym.matches("\\d{6}")) {
            entity.setDataYear(Integer.parseInt(ym.substring(0, 4)));
            entity.setDataMonth(Integer.parseInt(ym.substring(4, 6)));
        }

        entity.setStockCode(dto.getStockCode());
        entity.setStockName(dto.getStockName());
        entity.setIndustryType(dto.getIndustryType());
        entity.setRevenueCurrent(NumberUtils.toLong(dto.getRevenueCurrent()));
        entity.setRevenueLastMonth(NumberUtils.toLong(dto.getRevenueLastMonth()));
        entity.setRevenueLastYear(NumberUtils.toLong(dto.getRevenueLastYear()));
        entity.setRevenueMom(NumberUtils.toBigDecimal(dto.getRevenueMom()));
        entity.setRevenueYoy(NumberUtils.toBigDecimal(dto.getRevenueYoy()));
        entity.setCumulativeRevenueCurrent(NumberUtils.toLong(dto.getCumulativeRevenueCurrent()));
        entity.setCumulativeRevenueLastYear(NumberUtils.toLong(dto.getCumulativeRevenueLastYear()));
        entity.setCumulativeGrowth(NumberUtils.toBigDecimal(dto.getCumulativeGrowth()));
        entity.setNote(dto.getNote());

        return entity;
    }
}