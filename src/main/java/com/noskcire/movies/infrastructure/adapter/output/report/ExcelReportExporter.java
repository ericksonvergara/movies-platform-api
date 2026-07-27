package com.noskcire.movies.infrastructure.adapter.output.report;

import com.noskcire.movies.application.dto.report.*;
import com.noskcire.movies.domain.exception.ReportExportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Component
public class ExcelReportExporter {

    public byte[] exportMovieRanking(MovieRankingResult result) {
        return buildWorkbook(
                ReportTitles.MOVIE_RANKING,
                ReportHeaders.MOVIE_RANKING,
                result.data(),
                record -> new Object[]{
                        record.movieId(),
                        record.title(),
                        record.timesRented(),
                        record.unitsRented(),
                        record.averageUnits(),
                        record.revenue()
                }
        );
    }

    public byte[] exportClientRanking(ClientRankingResult result) {
        return buildWorkbook(
                ReportTitles.CLIENT_RANKING,
                ReportHeaders.CLIENT_RANKING,
                result.data(),
                record -> new Object[]{
                        record.clientId(),
                        record.name(),
                        record.email(),
                        record.totalRentals(),
                        record.totalSpent()
                });
    }

    public byte[] exportLateFeeRanking(LateFeeRankingResult result) {
        return buildWorkbook(
                ReportTitles.LATE_FEE_RANKING,
                ReportHeaders.LATE_FEE_RANKING,
                result.data(),
                record -> new Object[]{
                        record.clientId(),
                        record.clientName(),
                        record.totalLateFees(),
                        record.activeLateFees(),
                        record.pendingLateFees(),
                        record.paidLateFees(),
                        record.totalAmount()
                });
    }

    public byte[] exportReservationRanking(ReservationRankingResult result) {
        return buildWorkbook(
                ReportTitles.RESERVATION_RANKING,
                ReportHeaders.RESERVATION_RANKING,
                result.data(),
                record -> new Object[]{
                        record.clientId(),
                        record.clientName(),
                        record.totalReservations(),
                        record.activeReservations(),
                        record.notifiedReservations(),
                        record.fulfilledReservations(),
                        record.cancelledReservations(),
                        record.expiredReservations()
                });
    }

    public byte[] exportIncomeByPeriod(IncomeByPeriodResponse result) {
        return buildWorkbook(
                ReportTitles.INCOME_BY_PERIOD,
                ReportHeaders.INCOME_BY_PERIOD,
                List.of(result),
                record -> new Object[]{
                        record.startDate(),
                        record.endDate(),
                        record.totalRentals(),
                        record.totalIncome(),
                        record.averageRentalAmount()
                });
    }

    public byte[] exportRentalsByPeriod(RentalsByPeriodResult result) {
        return buildWorkbook(
                ReportTitles.RENTALS_BY_PERIOD,
                ReportHeaders.RENTALS_BY_PERIOD,
                result.data(),
                record -> new Object[]{
                        record.rentalDate(),
                        record.totalRentals()
                });
    }

    public byte[] exportProfitableMovies(ProfitableMovieResult result) {
        return buildWorkbook(
                ReportTitles.PROFITABLE_MOVIES,
                ReportHeaders.PROFITABLE_MOVIES,
                result.data(),
                record -> new Object[]{
                        record.movieId(),
                        record.title(),
                        record.totalUnits(),
                        record.totalIncome()
                }
        );
    }

    public byte[] exportStatistics(StatisticsResponse result) {
        return buildWorkbook(
                ReportTitles.STATISTICS,
                ReportHeaders.STATISTICS,
                List.of(result),
                record -> new Object[]{
                        record.totalIncome(),
                        record.totalRentals(),
                        record.totalReservations(),
                        record.totalLateFees(),
                        record.totalPersons(),
                        record.mostRentedMovie(),
                        record.topPerson()
                });
    }

    private <T> byte[] buildWorkbook(
            String sheetName,
            String[] headers,
            List<T> data,
            Function<T, Object[]> rowMapper) {
        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            Sheet sheet = workbook.createSheet(sheetName);

//            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (int rowIdx = 0; rowIdx < data.size(); rowIdx++) {

                Row row = sheet.createRow(rowIdx + 1);

                Object[] values = rowMapper.apply(data.get(rowIdx));

                for (int colIdx = 0; colIdx < values.length; colIdx++) {

                    Cell cell = row.createCell(colIdx);

                    setCellValue(cell, values[colIdx], dateStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            workbook.write(bos);

            return bos.toByteArray();

        } catch (IOException e) {
            throw new ReportExportException("Error al generar archivo Excel", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void setCellValue(Cell cell, Object value, CellStyle dateStyle) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof LocalDate date) {
//            cell.setCellValue(d.toString());
            cell.setCellValue(java.sql.Date.valueOf(date));
            cell.setCellStyle(dateStyle);
        } else if (value instanceof BigDecimal amount) {
            cell.setCellValue(amount.doubleValue());
        } else if (value instanceof Number n) {
            cell.setCellValue(n.doubleValue());
        }  else {
            cell.setCellValue(value.toString());
        }
    }

    private CellStyle createDateStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        CreationHelper helper = workbook.getCreationHelper();

        style.setDataFormat(
                helper.createDataFormat()
                        .getFormat("dd/MM/yyyy")
        );

        return style;
    }
}
