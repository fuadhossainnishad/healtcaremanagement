package com.healthcaremanagement.dashboard.service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AppointmentService appointmentService;
    private final MedicineService medicineService;
    private final BlogService blogService;
    private final RedisCacheService redisCacheService;

    @Cacheable(value = "dashboard", key = "#userId")
    public DashboardResponse getDashboard(String userId) {
        List<AppointmentSummary> upcomingAppointments = appointmentService.getUpcoming(userId);
        List<MedicineReminder> reminders = medicineService.getReminders(userId);
        List<BlogPost> latestBlogs = blogService.getLatest(5);
        // build response
        return DashboardResponse.builder()
                .upcomingAppointments(upcomingAppointments)
                .medicineReminders(reminders)
                .blogPosts(latestBlogs)
                // ... etc
                .build();
    }
}
