package com.otbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.otbs.model.HelpDeskExecutive;

@Repository
public interface HelpDeskExecutiveRepository extends JpaRepository<HelpDeskExecutive,Integer> {
     HelpDeskExecutive findByUsernameAndPassword(String username, String password);
}
