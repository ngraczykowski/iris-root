package com.silenteight.warehouse.report.persistence;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
class PersistenceService implements ReportPersistenceService {

  @NonNull
  private final ReportRepository reportRepository;

  @Override
  public ReportDto save(ReportRange range, String type, String id, String createdBy) {
    Report persisted = reportRepository.save(Report.of(range, type, id, createdBy));
    return persisted.toDto();
  }

  @Override
  public void generationFail(Long id) {
    reportRepository.getById(id).ifPresent(Report::failed);
  }

  @Override
  public void generationSuccessful(Long id) {
    reportRepository.getById(id).ifPresent(Report::done);
  }

  @Override
  public void generationStarted(Long id) {
    reportRepository.getById(id).ifPresent(Report::generating);
  }

  @Override
  public void zippingSuccessful(Long id) {
    reportRepository.getById(id).ifPresent(Report::zipped);
  }

  @Override
  public List<ReportDto> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    return reportRepository.getAllByCreatedAtBefore(offsetDateTime).stream()
        .map(Report::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteAll(List<ReportDto> reports) {
    reports.forEach(r -> reportRepository.deleteById(r.getId()));
  }

  @Override
  public ReportDto getReport(long id, String createdBy) {
    return reportRepository.getByIdAndCreatedBy(id, createdBy)
        .orElseThrow(() -> new ReportNotFoundException(id))
        .toDto();
  }
}
