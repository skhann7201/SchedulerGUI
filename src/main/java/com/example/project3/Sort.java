package com.example.project3;
import java.util.NoSuchElementException;

/**
 * This class provides methods, using selection sort, to sort for the
 * provider and appointment list.
 * @author Shahnaz Khan, Vy Nguyen
 */
public class Sort {

    /**
     * Sorts a list of appointments based on the specified key.
     * Sorting options include:
     * - 'D' for sorting by date.
     * - 'P' for sorting by patient profile.
     * - 'L' for sorting by location.
     *
     * @param list The list of appointments to be sorted.
     * @param key The sorting key: 'D' for date, 'P' for profile, 'L' for location.
     * @throws NoSuchElementException if the key is not recognized.
     */
    public static void appointment(List<Appointment> list, char key) {
        switch (key) {
            case 'D':  //by date
                sortByDate(list);
                break;
            case 'P':  //by profile
                sortByProfile(list);
                break;
            case 'L':  //by location
                sortByLocation(list);
                break;
            default:
                throw new NoSuchElementException("Wrong character input for sorting.");
        }
    }

    /**
     * Helper method: Generic swap method to swap elements in any list
     */
    private static <E> void swap(List<E> list, int index1, int index2) {
        E temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }

    /**
     * Compares two Provider objects based on their profiles. The comparison is
     * primarily done by the last name of the providers. If the last names are
     * equal, it further compares by the date of birth.
     *
     * @param p1 the first provider to compare
     * @param p2 the second provider to compare
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    private static int compareProviders(Provider p1, Provider p2) {
        Profile profile1 = p1.getProfile();
        Profile profile2 = p2.getProfile();

        // Compare by last name first
        int cmpLastName = profile1.getLastName().compareTo(profile2.getLastName());
        if (cmpLastName != 0) {
            return cmpLastName;
        }
        // if last name are equal, compare date of birth
        return profile1.getDOB().compareTo(profile2.getDOB());
    }

    /**
     * Sorts a list of providers based on their profiles. The sorting is primarily
     * based on the last name of the provider, and if the last names are equal,
     * it then sorts based on the date of birth.
     *
     * @param list the list of providers to be sorted
     */
    public static void provider(List<Provider> list) {
        int size = list.size();

        for(int i = 0; i < size; i++){
            int minIndex = i;

            for(int currentIndex  = minIndex + 1; currentIndex < size; currentIndex++){
                if (compareProviders(list.get(minIndex), list.get(currentIndex)) > 0){
                    minIndex = currentIndex; // update the minIndex
                }
            }

            // Swap the current element with the found minimum element
            if (minIndex != i) {
                swap(list, i, minIndex);
            }
        }

    }

    /**
     * This method sorts the Appointment list by appointment date, time, then provider’s name.
     * If date is the same, then compare time, if time comparison is the same, then compare
     * provider's name.
     * @param list, the list of appointments
     */
    private static void sortByDate(List<Appointment> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (list.get(j) != null && list.get(minIndex) != null) {
                    int dateComparison = list.get(j).getDate().compareTo(list.get(minIndex).getDate());
                    if (dateComparison < 0) {
                        minIndex = j;
                    } else if (dateComparison == 0) {
                        int providerComparison = list.get(j).getProvider().compareTo(list.get(minIndex).getProvider());
                        if (providerComparison < 0) {
                            minIndex = j;
                        } else if (providerComparison == 0) {
                            String providerNameA = list.get(j).getProvider().toString();
                            String providerNameB = list.get(minIndex).getProvider().toString();
                            if (providerNameA.compareTo(providerNameB) < 0) {
                                minIndex = j;
                            }
                        }
                    }
                }
            }
            // Swap if a new minimum is found
            if (minIndex != i) {
                swap(list, i, minIndex);
            }
        }
    }

    /**
     * Sorts the list ordered by patient profile, date/timeslot
     * @param list, the list of Appointments to be sorted
     */
    private static void sortByProfile(List<Appointment> list){
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (list.get(j) != null && list.get(minIndex) != null) {
                    int profileComparison = list.get(j).getProfile().compareTo(list.get(minIndex).getProfile());
                    if (profileComparison < 0) {
                        minIndex = j;
                    } else if (profileComparison == 0) {
                        int dateComparison = list.get(j).getDate().compareTo(list.get(minIndex).getDate());
                        if (dateComparison < 0) {
                            minIndex = j;
                        } else if (dateComparison == 0) {
                            String providerNameA = list.get(j).getProvider().toString();
                            String providerNameB = list.get(minIndex).getProvider().toString();
                            if (providerNameA.compareTo(providerNameB) < 0) {
                                minIndex = j;
                            }
                        }
                    }
                }
            }
            // Swap if a new minimum is found
            if (minIndex != i) {
                swap(list,i, minIndex);
            }
        }
    }

    /**
     * Sorting the list ordered by county, date/timeslot.
     * If county comparison is equal, sort by date, if date comparison is equal
     * sort by timeslot.
     * @param list, the list of Appointments to be sorted.
     */
    private static void sortByLocation(List<Appointment> list){
        int size = list.size();
        for (int i = 0; i < size; i++) {
            int minIndex = i;
            for (int j = i + 1; j < size; j++) {
                Appointment minAppointment = list.get(minIndex);
                Appointment currentAppointment = list.get(j);

                Provider minProvider =  (Provider) minAppointment.getProvider();
                Provider currentProvider =  (Provider) currentAppointment.getProvider();

                if (list.get(j) != null && list.get(minIndex) != null) {
                    int locationComparison = currentProvider.getLocation().getCounty().compareTo(minProvider.getLocation().getCounty());
                    if (locationComparison < 0) {
                        minIndex = j;
                    } else if (locationComparison == 0) {
                        int dateComparison = currentAppointment.getDate().compareTo(minAppointment.getDate());
                        if (dateComparison < 0) {
                            minIndex = j;
                        } else if (dateComparison == 0) {
                            int timeslotComparison = currentAppointment.getTimeslot().compareTo(minAppointment.getTimeslot());
                            if (timeslotComparison < 0) {
                                minIndex = j;
                            } else if (timeslotComparison == 0) {
                                String providerNameA = currentProvider.toString();
                                String providerNameB = minProvider.toString();
                                if (providerNameA.compareTo(providerNameB) < 0) {
                                    minIndex = j;
                                }
                            }
                        }
                    }
                }
            }
            // Swap if a new minimum is found
            if (minIndex != i) {
                swap(list,i, minIndex);
            }
        }
    }
}