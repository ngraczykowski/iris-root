# Comment template context

The comment template evaluation gets the context created from the following classes, with AlertContext being the root:

    class AlertContext {
    
        // The source system alert identifier.
        String alertId;
        
        // The arbitrary data for comment received from Bridge. 
        Map<String, Object> commentInput;
        
        // The recommended action as returned from Governance.
        String recommendedAction;
        
        // The list of matches in the alert.
        List<MatchContext> matches;
    }
    
    class MatchContext {
    
        // The source system match identifier.
        String matchId;
        
        // The solution the Governance returned for the feature vector of this match.
        String solution;
        
        // The arbitrary data received from the Governance for this match solution.
        Map<String, Object> reason;
        
        // Map of a category name to its value for the match.
        Map<String, String> categories;
        
        // Map of feature name to the feature value context.
        Map<String, FeatureContext> features;
    }

    class FeatureContext {
    
        // The agent config, e.g., `agents/name/versions/1.0.0/configs/1`.
        String agentConfig;
        
        // The agent name, i.e., the first part of the agent config, e.g., `agents/name`.
        String agentName;
        
        // The solution received from the agent.
        String solution;
        
        // The arbitrary data received from the agent for this feature.
        Map<String, Object> reason;
    }
